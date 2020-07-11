package site.code4fun.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import site.code4fun.constant.Queue;
import site.code4fun.constant.Role;
import site.code4fun.constant.Status;
import site.code4fun.entity.Classes;
import site.code4fun.entity.Student;
import site.code4fun.entity.User;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.repository.StudentRepository;
import site.code4fun.repository.UserRepository;
import site.code4fun.repository.jdbc.JStudentRepository;
import site.code4fun.util.StringUtils;

@Service
public class StudentService extends BaseService{
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private JStudentRepository jStudentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private QueueService queueService;

	@Autowired
	private ClassService classService;

	public List<StudentDTO> getAll(Long classId, Long groupId, Long organizationId){
		List<Classes> lstClass = classService.getByGroupId(groupId);
		Map<Long, String> mapClass = lstClass.stream().collect(Collectors.toMap(Classes::getId, Classes::getName));
		List<Long> idsClass = new ArrayList<Long>(mapClass.keySet());

		if (null != classId && idsClass.contains(classId)) {
			idsClass = Arrays.asList(classId);
		}
		return jStudentRepository.findStudentByClassId(idsClass);
	}

	public StudentDTO getById(Long id) throws Exception {
		StudentDTO dto = jStudentRepository.findById(id);
		List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		if(dto == null || !classIds.contains(dto.getClassId())) throw new Exception("Không tồn tại!");
		return dto;
		
	}

	public Student create(StudentDTO s) throws Exception {
		if (StringUtils.isNull(s.getParentPhoneOrEmail())) throw new Exception("Email phụ huynh không được bỏ trống!");
		if (StringUtils.isNull(s.getName())) throw new Exception("Tên học sinh không được bỏ trống!");
		
		String passWord = StringUtils.randomString();
		User u = userRepository.findByUserName(s.getParentPhoneOrEmail());
		if (null == u) {
			u = User.builder()
					.username(s.getParentPhoneOrEmail())
					.address(s.getAddress())
					.createdDate(new Timestamp(System.currentTimeMillis()))
					.fullName(s.getParentName())
					.password(new BCryptPasswordEncoder().encode(passWord))
					.role(Role.ROLE_USER.getVal())
					.email(s.getParentPhoneOrEmail())
					.status(Status.PENDING.getVal()).build();
			u = userRepository.saveAndFlush(u);
			
//			Thêm vào queue gửi mật khẩu cho tài khoản mới đăng ký
			StringBuilder mailContent = new StringBuilder("Bạn đã được đăng ký tài khoản phụ huynh cho ");
			mailContent.append(s.getName());
			mailContent.append(". Vui lòng cài app và sử dụng tên tài khoản là địa chỉ email và mật khẩu: ");
			mailContent.append(passWord);
			mailContent.append(" để đăng nhập!");
			
			Map<String, String> mailMess = new HashMap<>();
			mailMess.put("receiver", s.getParentPhoneOrEmail());
			mailMess.put("subject", "Tạo tài khoản thành công");
			mailMess.put("content", mailContent.toString());
			String mesToQueue = new Gson().toJson(mailMess);
			queueService.sendToQueue(Queue.QUEUE_MAIL, mesToQueue);
		}

		Student student = Student.builder()
				.address(s.getAddress())
				.name(s.getName())
				.dateOfBirth(s.getDateOfBirth())
				.email(s.getEmail())
				.phone(s.getPhone())
				.classId(s.getClassId())
				.parentId(u.getId())
				.note(s.getNote()).build();
				
		student = studentRepository.saveAndFlush(student);
		return student;
	}

	public Student update(StudentDTO s) throws Exception {
		Optional<Student> item = studentRepository.findById(s.getId());
		if (!item.isPresent())
			throw new Exception("Student not found!");
		
		if (StringUtils.isNull(s.getParentPhoneOrEmail())) throw new Exception("Email phụ huynh không được bỏ trống!");
		if (StringUtils.isNull(s.getName())) throw new Exception("Tên học sinh không được bỏ trống!");
		
		
		User u = userRepository.findByUserName(s.getParentPhoneOrEmail());
		if (null == u) {
			String passWord = StringUtils.randomString();
			u = User.builder()
					.username(s.getParentPhoneOrEmail())
					.address(s.getAddress())
					.createdDate(new Timestamp(System.currentTimeMillis()))
					.fullName(s.getParentName())
					.password(new BCryptPasswordEncoder().encode(passWord))
					.role(Role.ROLE_USER.getVal())
					.status(Status.PENDING.getVal()).build();
			u = userRepository.saveAndFlush(u);
			
//			Thêm vào queue gửi mật khẩu cho tài khoản mới đăng ký
			StringBuilder mailContent = new StringBuilder("Bạn đã được đăng ký tài khoản phụ huynh cho ");
			mailContent.append(s.getName());
			mailContent.append(". Vui lòng cài app và sử dụng tên tài khoản là địa chỉ email và mật khẩu: ");
			mailContent.append(passWord);
			mailContent.append(" để đăng nhập!");
			
			Map<String, String> mailMess = new HashMap<>();
			mailMess.put("receiver", s.getParentPhoneOrEmail());
			mailMess.put("subject", "Tạo tài khoản thành công");
			mailMess.put("content", mailContent.toString());
			String mesToQueue = new Gson().toJson(mailMess);
			queueService.sendToQueue(Queue.QUEUE_MAIL, mesToQueue);
		}

		Student student = Student.builder()
				.id(s.getId())
				.address(s.getAddress())
				.name(s.getName())
				.dateOfBirth(s.getDateOfBirth())
				.email(s.getEmail())
				.phone(s.getPhone())
				.classId(s.getClassId())
				.parentId(s.getParentId())
				.note(s.getNote()).build();
		return studentRepository.saveAndFlush(student);
	}

	public boolean delete(Long studentId, Long classId) throws Exception {
		Optional<Student> item = studentRepository.findById(studentId);
		if(null != classId) {
			if(item.isPresent() && item.get().getClassId() == classId ) {
				item.get().setClassId(null);
				studentRepository.save(item.get());
				return true;
			}
		}else {			
			List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
			if(item.isPresent() & classIds.contains(item.get().getClassId())) {
				studentRepository.deleteById(studentId);	
				return true;
			}
		}
		throw new Exception("Không có quyền xóa!");
	}
}

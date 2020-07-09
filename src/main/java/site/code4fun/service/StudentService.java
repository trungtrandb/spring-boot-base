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
import site.code4fun.entity.ParentStudent;
import site.code4fun.entity.Student;
import site.code4fun.entity.StudentClass;
import site.code4fun.entity.User;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.repository.ParentStudentRepository;
import site.code4fun.repository.StudentClassRepository;
import site.code4fun.repository.StudentRepository;
import site.code4fun.repository.UserRepository;
import site.code4fun.repository.jdbc.JStudentRepository;
import site.code4fun.util.StringUtils;

@Service
public class StudentService {
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private JStudentRepository jStudentRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ParentStudentRepository parentStudentRepository;

	@Autowired
	private StudentClassRepository studentClassRepository;

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
//		List<Student> lst = studentRepository.findStudentByClassId(idsClass, limit, offset);
		
//		List<StudentDTO> lstDTO = new ArrayList<>();
//		lst.forEach(s -> {
//			StudentDTO dto = StudentDTO.builder()
//					.id(s.getId())
//					.dateOfBirth(s.getDateOfBirth())
//					.email(s.getEmail())
//					.address(s.getAddress())
//					.phone(s.getPhone())
//					.note(s.getNote()).build();
//			lstDTO.add(dto);
//		});
		return jStudentRepository.findStudentByClassId(idsClass);
	}

	public StudentDTO getById(Long id) throws Exception {
		Optional<Student> s = studentRepository.findById(id);
		if (!s.isPresent())
			throw new Exception("Student not found");
		
		StudentDTO dto = StudentDTO.builder()
				.address(s.get().getAddress())
				.phone(s.get().getPhone())
				.name(s.get().getName())
				.note(s.get().getNote())
				.dateOfBirth(s.get().getDateOfBirth())
				.email(s.get().getEmail())
				.id(s.get().getId())
				.build();
		
		List<User> u = jStudentRepository.findParentByStudentId(id);
		if(u.size() > 0) {
			dto.setParentName(u.get(0).getFullName());
			dto.setParentPhoneOrEmail(u.get(0).getUsername());
		}
		
		List<Classes> clazz = jStudentRepository.findClassByStudentId(id);
		Long[] idsClass = new Long[clazz.size()];
		for(int i = 0; i < clazz.size(); i++ ) {
			idsClass[i] = clazz.get(i).getId();
		}
		dto.setClasses(idsClass);
		
		return dto;
	}

	public Student create(StudentDTO s) throws Exception {
		if (StringUtils.isNull(s.getParentPhoneOrEmail())) throw new Exception("Parent phone or mail is null!");
		if (StringUtils.isNull(s.getName())) throw new Exception("Student name is null!");
		
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
				.note(s.getNote()).build();
		student = studentRepository.saveAndFlush(student);
		
		ParentStudent ps = ParentStudent.builder().studentId(student.getId()).userId(u.getId()).build();
		parentStudentRepository.save(ps);

		if (s.getClasses() != null) {
			List<StudentClass> lstStudentClass = new ArrayList<>();
			for (int i = 0; i < s.getClasses().length; i++) {
				StudentClass sc = StudentClass.builder().studentId(student.getId()).classId(s.getClasses()[i]).build();
				lstStudentClass.add(sc);
			}
			studentClassRepository.saveAll(lstStudentClass);
		}

		return student;
	}

	public Student update(StudentDTO s) throws Exception {
		Optional<Student> item = studentRepository.findById(s.getId());
		if (!item.isPresent())
			throw new Exception("Student not found!");
		
		if (StringUtils.isNull(s.getParentPhoneOrEmail())) throw new Exception("Parent phone or mail is null!");
		if (StringUtils.isNull(s.getName())) throw new Exception("Student name is null!");
		
		
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
				.note(s.getNote()).build();
		student = studentRepository.saveAndFlush(student);
		
		parentStudentRepository.deleteByStudentId(s.getId());
		ParentStudent ps = ParentStudent.builder().studentId(student.getId()).userId(u.getId()).build();
		parentStudentRepository.save(ps);

		studentClassRepository.deleteByStudentId(s.getId());
		if (s.getClasses() != null) {
			List<StudentClass> lstStudentClass = new ArrayList<>();
			for (int i = 0; i < s.getClasses().length; i++) {
				StudentClass sc = StudentClass.builder().studentId(student.getId()).classId(s.getClasses()[i]).build();
				lstStudentClass.add(sc);
			}
			studentClassRepository.saveAll(lstStudentClass);
		}

		return student;
	}

	public boolean delete(Long studentId, Long classId) throws Exception {
		Optional<Student> item = studentRepository.findById(studentId);
		if (!item.isPresent())
			throw new Exception("Student not found!");
		if(null != classId) {
			studentClassRepository.deleteByStudentIdAndClassId(studentId, classId);;
		}else {
			studentRepository.deleteById(studentId);
			parentStudentRepository.deleteByStudentId(studentId);
		}
		return true;
	}
}

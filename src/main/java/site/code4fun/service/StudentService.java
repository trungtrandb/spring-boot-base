package site.code4fun.service;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import site.code4fun.constant.Queue;
import site.code4fun.constant.Role;
import site.code4fun.constant.Status;
import site.code4fun.entity.Classes;
import site.code4fun.entity.Student;
import site.code4fun.entity.User;
import site.code4fun.entity.dto.ChooseStudentDTO;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.util.StringUtils;

@Service
public class StudentService extends BaseService{

	public List<StudentDTO> getAll(Long classId, String name){
		List<Classes> lstClass = getCurrentClasses();
		Map<Long, String> mapClass = lstClass.stream().collect(Collectors.toMap(Classes::getId, Classes::getName));
		List<Long> idsClass = new ArrayList<>(mapClass.keySet());

		if (null != classId) {
			if (!idsClass.contains(classId)) return new ArrayList<>();
			idsClass = Collections.singletonList(classId);
		}
		return jStudentRepository.findByClassId(idsClass, name);
	}

	public StudentDTO getById(Long id) throws Exception {
		StudentDTO dto = jStudentRepository.findById(id);
		List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		if(dto == null || !classIds.contains(dto.getClassId())) throw new Exception("Không tồn tại!");
		return dto;
		
	}
	
	public Optional<Student> getStudentById(Long id){
		return studentRepository.findById(id);
	}
	
	public ChooseStudentDTO getInfoStudent(Long id){
		return jStudentRepository.getInfoStudent(id);
	}

	public Student create(StudentDTO s) throws Exception {
		if (StringUtils.isNull(s.getName())) throw new Exception("Tên học sinh không được bỏ trống!");
		if (StringUtils.isNull(s.getStudentCode())) throw new Exception("Mã học sinh không được bỏ trống!");	
		if (StringUtils.isNull(s.getParentPhoneOrEmail())) throw new Exception("Email phụ huynh không được bỏ trống!");
		if (s.getClassId() == null) throw new Exception("Chưa chọn lớp cho học sinh!");
		
		
        String normalizeCode = Normalizer.normalize(s.getStudentCode(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replace(" ", "");
        List<Long> idsClass = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
        
        List<Student> lst = studentRepository.findByStudentCode(normalizeCode);
        for(Student _st : lst) {
        	if(idsClass.contains(_st.getClassId())) throw new Exception("Mã học sinh phải là duy nhất!");
        }

        s.setStudentCode(normalizeCode);
		
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
					.status(Status.PENDING).build();
			u = userRepository.saveAndFlush(u);
			
			Map<String, String> mailMess = new HashMap<>();
			mailMess.put("receiver", s.getParentPhoneOrEmail());
			mailMess.put("subject", "Tạo tài khoản thành công");
			String mailContent = "Bạn đã được đăng ký tài khoản phụ huynh cho " + s.getName() +
					". Vui lòng cài app và sử dụng tên tài khoản là địa chỉ email và mật khẩu: " +
					passWord +
					" để đăng nhập!";
			mailMess.put("content", mailContent);
			String mesToQueue = new Gson().toJson(mailMess);
			queueService.sendToQueue(Queue.QUEUE_MAIL, mesToQueue);
		}

		Student student = Student.builder()
				.address(s.getAddress())
				.studentCode(s.getStudentCode())
				.name(s.getName())
				.dateOfBirth(s.getDateOfBirth())
				.email(s.getEmail())
				.phone(s.getPhone())
				.avatar(s.getAvatar())
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
		
		if (StringUtils.isNull(s.getName())) throw new Exception("Tên học sinh không được bỏ trống!");
		if (StringUtils.isNull(s.getStudentCode())) throw new Exception("Mã học sinh không được bỏ trống!");	
		if (StringUtils.isNull(s.getParentPhoneOrEmail())) throw new Exception("Email phụ huynh không được bỏ trống!");
		if (s.getClassId() == null) throw new Exception("Chưa chọn lớp cho học sinh!");
		
		
        String normalizeCode = Normalizer.normalize(s.getStudentCode(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").replace(" ", "");
        List<Long> idsClass = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
        
        List<Student> lst = studentRepository.findByStudentCode(normalizeCode);
        for(Student _st : lst) {
        	if(idsClass.contains(_st.getClassId()) && !_st.getId().equals(s.getId())) throw new Exception("Mã học sinh phải là duy nhất!");
        }		
		
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
					.status(Status.PENDING).build();
			u = userRepository.saveAndFlush(u); 
			
			Map<String, String> mailMess = new HashMap<>();
			mailMess.put("receiver", s.getParentPhoneOrEmail());
			mailMess.put("subject", "Tạo tài khoản thành công");
			String mailContent = "Bạn đã được đăng ký tài khoản phụ huynh cho " + s.getName() +
					". Vui lòng cài app và sử dụng tên tài khoản là địa chỉ email và mật khẩu: " +
					passWord +
					" để đăng nhập!";
			mailMess.put("content", mailContent);
			String mesToQueue = new Gson().toJson(mailMess);
			queueService.sendToQueue(Queue.QUEUE_MAIL, mesToQueue);
		}

		Student student = Student.builder()
				.id(s.getId())
				.address(s.getAddress())
				.name(s.getName())
				.avatar(s.getAvatar())
				.dateOfBirth(s.getDateOfBirth())
				.email(s.getEmail())
				.phone(s.getPhone())
				.classId(s.getClassId())
				.studentCode(normalizeCode)
				.parentId(u.getId())
				.note(s.getNote()).build();
		return studentRepository.saveAndFlush(student);
	}

	public boolean delete(Long studentId) throws Exception {
		Optional<Student> item = studentRepository.findById(studentId);
		List<Long> lstClass = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		if(item.isPresent() && lstClass.contains(item.get().getClassId())) {
			item.get().setClassId(null);
			checkinRepository.deleteByStudentId(studentId);
			pointRepository.deleteByStudentId(studentId);
			studentRepository.deleteById(studentId);
			return true;
		}
		throw new Exception("Không có quyền xóa!");
	}

	public List<Student> getByCurrentParent() {
		return studentRepository.findByParentId(getCurrentId());
	}
	
	public List<ChooseStudentDTO> getListChooseSt() {
		return jStudentRepository.getListChooseSt(getCurrentId());
	}

	public Student updateStudent(Student s) throws Exception {
		Optional<Student> item = studentRepository.findById(s.getId());
		if (!item.isPresent())
			throw new Exception("Student not found!");

		Student student = Student.builder()
				.id(s.getId())
				.address(s.getAddress())
				.studentCode(s.getStudentCode())
				.name(s.getName())
				.avatar(s.getAvatar())
				.dateOfBirth(s.getDateOfBirth())
				.email(s.getEmail())
				.phone(s.getPhone())
				.classId(s.getClassId())
				.parentId(s.getParentId())
				.note(s.getNote()).build();
		return studentRepository.saveAndFlush(student);
	}
}

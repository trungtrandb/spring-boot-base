package site.code4fun.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import site.code4fun.constant.Queue;
import site.code4fun.constant.Role;
import site.code4fun.constant.Status;
import site.code4fun.entity.*;
import site.code4fun.entity.dto.CheckinDTO;
import site.code4fun.entity.dto.ChooseStudentDTO;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.util.CalculatorUtil;
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
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$");
		if((!StringUtils.isNull(s.getEmail()) && !pattern.matcher(s.getEmail()).matches()) || !pattern.matcher(s.getParentPhoneOrEmail()).matches()) throw new Exception("Email không đúng định dạng!");
		
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
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$");
		if((!StringUtils.isNull(s.getEmail()) && !pattern.matcher(s.getEmail()).matches()) || !pattern.matcher(s.getParentPhoneOrEmail()).matches()) throw new Exception("Email không đúng định dạng!");
		
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
		if (!item.isPresent()) throw new Exception("Student not found!");

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

    public Object viewPoint(Long studentId) {
		List<Point> lstPoint = pointRepository.findByStudentId(studentId);
		List<Long> subjectIds = lstPoint.stream().map(Point::getSubjectId).collect(Collectors.toList());
		List<Subject> lstSubject = subjectRepository.findAllById(subjectIds);
		
		
		Map<Long, HashMap<String, List<Float>>> mapSubject = new HashMap<>();
		lstPoint.forEach(_point -> {
			HashMap<String, List<Float>> mapPoint = new HashMap<>();
			List<Float> pointMulti1Sem1 = new ArrayList<>();
			List<Float> pointMulti2Sem1 = new ArrayList<>();
			List<Float> pointMulti3Sem1 = new ArrayList<>();
			List<Float> pointMulti2Sem2 = new ArrayList<>();
			List<Float> pointMulti3Sem2 = new ArrayList<>();
			List<Float> pointMulti1Sem2 = new ArrayList<>();
			if(!mapSubject.containsKey(_point.getSubjectId())) {
				if(_point.getSem() == 1) {
					if(_point.getMultiple() == 1) pointMulti1Sem1.add(_point.getPoint());
					if(_point.getMultiple() == 2) pointMulti2Sem1.add(_point.getPoint());
					if(_point.getMultiple() == 3) pointMulti3Sem1.add(_point.getPoint());
				}else {
					if(_point.getMultiple() == 1) pointMulti1Sem2.add(_point.getPoint());
					if(_point.getMultiple() == 2) pointMulti2Sem2.add(_point.getPoint());
					if(_point.getMultiple() == 3) pointMulti3Sem2.add(_point.getPoint());
				}
				mapPoint.put("pointMulti1Sem1", pointMulti1Sem1);
				mapPoint.put("pointMulti2Sem1", pointMulti2Sem1);
				mapPoint.put("pointMulti3Sem1", pointMulti3Sem1);
				mapPoint.put("pointMulti1Sem2", pointMulti1Sem2);
				mapPoint.put("pointMulti2Sem2", pointMulti2Sem2);
				mapPoint.put("pointMulti3Sem2", pointMulti3Sem2);
			}else {
				mapPoint = mapSubject.get(_point.getSubjectId());
				if(_point.getSem() == 1) {
					if(_point.getMultiple() == 1) {
						pointMulti1Sem1 = mapPoint.get("pointMulti1Sem1");
						pointMulti1Sem1.add(_point.getPoint());
						mapPoint.put("pointMulti1Sem1", pointMulti1Sem1);
					}
					
					if(_point.getMultiple() == 2) {
						pointMulti2Sem1 = mapPoint.get("pointMulti2Sem1");
						pointMulti2Sem1.add(_point.getPoint());
						mapPoint.put("pointMulti2Sem1", pointMulti2Sem1);
					}
					if(_point.getMultiple() == 3) {
						pointMulti3Sem1 = mapPoint.get("pointMulti3Sem1");
						pointMulti3Sem1.add(_point.getPoint());
						mapPoint.put("pointMulti3Sem1", pointMulti3Sem1);
					}
				}else {
					if(_point.getMultiple() == 1) {
						pointMulti1Sem2 = mapPoint.get("pointMulti1Sem2");
						pointMulti1Sem2.add(_point.getPoint());
						mapPoint.put("pointMulti1Sem2", pointMulti1Sem2);
					}
					if(_point.getMultiple() == 2) {
						pointMulti2Sem2 = mapPoint.get("pointMulti2Sem2");
						pointMulti2Sem2.add(_point.getPoint());
						mapPoint.put("pointMulti2Sem2", pointMulti2Sem2);
					}
					if(_point.getMultiple() == 3) {
						pointMulti3Sem2 = mapPoint.get("pointMulti3Sem2");
						pointMulti3Sem2.add(_point.getPoint());
						mapPoint.put("pointMulti3Sem2", pointMulti3Sem2);
					}
				}
			}
			mapSubject.put(_point.getSubjectId(), mapPoint);
		});
		
		List<HashMap<String, Object>> lstRes = new ArrayList<>();
		lstSubject.forEach(_subject -> {
			HashMap<String, List<Float>> mapPoint = mapSubject.get(_subject.getId());

			HashMap<String, Object> mapRes = new HashMap<String, Object>();
			double totalPoint = 0;
			totalPoint += CalculatorUtil.sumPointFromList(mapPoint.get("pointMulti1Sem1"), 1);
			totalPoint += CalculatorUtil.sumPointFromList(mapPoint.get("pointMulti2Sem1"), 2);
			totalPoint += CalculatorUtil.sumPointFromList(mapPoint.get("pointMulti3Sem1"), 3);
			
			double avg1 = totalPoint / (mapPoint.get("pointMulti1Sem1").size() + mapPoint.get("pointMulti2Sem1").size() * 2 + mapPoint.get("pointMulti3Sem1").size() * 3);
	        BigDecimal bd1 = new BigDecimal(avg1).setScale(2, RoundingMode.HALF_UP);
	        
			mapRes.put("subjectName", _subject.getName());
			mapRes.put("pointMulti1Sem1", StringUtils.stringFromList(mapPoint.get("pointMulti1Sem1")));
			mapRes.put("pointMulti2Sem1", StringUtils.stringFromList(mapPoint.get("pointMulti2Sem1")));
			mapRes.put("pointMulti3Sem1", StringUtils.stringFromList(mapPoint.get("pointMulti3Sem1")));
			mapRes.put("pointAvgSem1", bd1.doubleValue());
			
			totalPoint = 0;
			totalPoint += CalculatorUtil.sumPointFromList(mapPoint.get("pointMulti1Sem2"), 1);
			totalPoint += CalculatorUtil.sumPointFromList(mapPoint.get("pointMulti2Sem2"), 2);
			totalPoint += CalculatorUtil.sumPointFromList(mapPoint.get("pointMulti3Sem2"), 3);
			double avg2 = totalPoint / (mapPoint.get("pointMulti1Sem1").size() + mapPoint.get("pointMulti2Sem1").size() * 2 + mapPoint.get("pointMulti3Sem1").size() * 3);
			BigDecimal bd2 = new BigDecimal(avg2).setScale(2, RoundingMode.HALF_UP);

			mapRes.put("pointMulti1Sem2", StringUtils.stringFromList(mapPoint.get("pointMulti1Sem2")));
			mapRes.put("pointMulti2Sem2", StringUtils.stringFromList(mapPoint.get("pointMulti2Sem2")));
			mapRes.put("pointMulti3Sem2", StringUtils.stringFromList(mapPoint.get("pointMulti3Sem2")));
			mapRes.put("pointAvgSem2", bd2.doubleValue());
			
			avg2 = avg2 == 0 || avg1 == 0 ? avg2 += avg1 : (avg2 += avg1) / 2;
			
			bd2 = new BigDecimal(avg2).setScale(2, RoundingMode.HALF_UP);
			mapRes.put("pointAvgTotal",bd2.doubleValue() );
			lstRes.add(mapRes);
		});
		return lstRes;
    }

	public List<CheckinDTO> viewCheckin(Long studentId) {
		int limit = 10;
		int offset = 0;
		return jCheckinRepository.getCheckinByStudentId(studentId, limit, offset);
		
	}
}

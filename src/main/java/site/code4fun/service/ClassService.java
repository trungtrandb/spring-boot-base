package site.code4fun.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Point;
import site.code4fun.entity.User;
import site.code4fun.entity.dto.ClassDTO;
import site.code4fun.entity.dto.PointDTO;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.util.StringUtils;

@Service
public class ClassService extends BaseService{
	
	public List<Classes> getByGroupId(Long id){
		if(null != id) {
			return classRepository.findByGroupId(Arrays.asList(id));
		}else {
			return getCurrentClasses();
		}
	}
	
	public Boolean authorizeClass(Long groupClassId) {
		return true;
	}
	
	public Classes insert(ClassDTO c) throws Exception {
		if(StringUtils.isNull(c.getName())) throw new Exception("Tên lớp là bắt buộc nhập");
		if(null == c.getGroupClassId()) throw new Exception("Khối lớp là bắt buộc chọn");
		if(null == c.getOwnerId()) throw new Exception("Giáo viên chủ nhiệm là bắt buộc chọn");
		Optional<GroupClass> group = groupClassRepository.findById(c.getGroupClassId());
		Optional<User> user = userRepository.findById(c.getOwnerId());
		
		Classes clazz = Classes.builder()
				.name(c.getName())
				.note(c.getNote())
				.groupClass(group.get())
				.owner(user.get())
				.build();
		return classRepository.saveAndFlush(clazz);
	}
	
	public void delete(Long id) throws Exception {
		Optional<Classes> item = classRepository.findById(id);
		if (!item.isPresent()) throw new Exception("Class not found!");
		classRepository.deleteById(id);
	}

	public List<PointDTO> getPoint(Long classId, Long subjectId, Byte sem, Byte numOfTest){
		List<StudentDTO> lstStudent = jStudentRepository.findStudentByClassId(Arrays.asList(classId));
		List<Long> studentIds = lstStudent.stream().map(StudentDTO::getId).collect(Collectors.toList());
		Map<Long, PointDTO> mapPoint = jPointRepository.getPoint(StringUtils.stringFromList(studentIds), subjectId, sem, numOfTest);
		
		List<PointDTO> lstPointRes  = lstStudent.stream().map(_st -> {
			PointDTO dto;
			if(mapPoint.containsKey(_st.getId())) {
				dto = mapPoint.get(_st.getId());
			}else {
				dto = new PointDTO();
				dto.setSubjectId(subjectId);
				dto.setSem(sem);
				dto.setNumOfTest(numOfTest);
			}
			dto.setStudentId(_st.getId());
			dto.setStudentName(_st.getName());
			dto.setStudentCode(_st.getStudentCode());
			return dto;
		}).collect(Collectors.toList());
		return lstPointRes;
	}

	public List<Point> updatePoint(PointDTO point) {
		String multi1 = StringUtils.cleanToFloat(point.getPointMulti1());
		String multi2 = StringUtils.cleanToFloat(point.getPointMulti2());
		String multi3 = StringUtils.cleanToFloat(point.getPointMulti3());
		
		String[] lstPointMulti1 = multi1.split(" ");
		String[] lstPointMulti2 = multi2.split(" ");
		String[] lstPointMulti3 = multi3.split(" ");
		Long currentId = getCurrentId();

		pointRepository.deleteOldPoint(point.getStudentId(), point.getSubjectId(), point.getSem(), point.getNumOfTest());
		List<Point> lstPoint = new ArrayList<>();
		for(String _item : lstPointMulti1) {
			if(StringUtils.isNull(_item)) continue;
			Point newPoint = new Point();
			newPoint.setStudentId(point.getStudentId());
			newPoint.setSubjectId(point.getSubjectId());
			newPoint.setMultiple((byte) 1);
			newPoint.setPoint(StringUtils.round1(_item));
			newPoint.setCreatedBy(currentId);
			newPoint.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			newPoint.setNumOfTest(point.getNumOfTest());
			newPoint.setSem(point.getSem());
			lstPoint.add(newPoint);
		}
		
		for(String _item : lstPointMulti2) {
			if(StringUtils.isNull(_item)) continue;
			Point newPoint = new Point();
			newPoint.setStudentId(point.getStudentId());
			newPoint.setSubjectId(point.getSubjectId());
			newPoint.setMultiple((byte) 2);
			newPoint.setPoint(StringUtils.round1(_item));
			newPoint.setCreatedBy(currentId);
			newPoint.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			newPoint.setNumOfTest(point.getNumOfTest());
			newPoint.setSem(point.getSem());
			lstPoint.add(newPoint);
		}
		
		for(String _item : lstPointMulti3) {
			if(StringUtils.isNull(_item)) continue;
			Point newPoint = new Point();
			newPoint.setStudentId(point.getStudentId());
			newPoint.setSubjectId(point.getSubjectId());
			newPoint.setMultiple((byte) 3);
			newPoint.setPoint(StringUtils.round1(_item));
			newPoint.setCreatedBy(currentId);
			newPoint.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			newPoint.setNumOfTest(point.getNumOfTest());
			newPoint.setSem(point.getSem());
			lstPoint.add(newPoint);
		}

		pointRepository.saveAll(lstPoint);
		return lstPoint;
	}

	public Optional<Classes> getById(Long id) {
		// TODO Auto-generated method stub
		return classRepository.findById(id);
	}
}

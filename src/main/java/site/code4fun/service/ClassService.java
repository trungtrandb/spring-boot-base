package site.code4fun.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.User;
import site.code4fun.entity.dto.ClassDTO;
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

	public Optional<Classes> getById(Long id) {
		// TODO Auto-generated method stub
		return classRepository.findById(id);
	}
}

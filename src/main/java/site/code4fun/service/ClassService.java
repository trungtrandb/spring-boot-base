package site.code4fun.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.User;
import site.code4fun.entity.dto.ClassDTO;

@Service
public class ClassService extends BaseService{
	
	public List<Classes> getByGroupId(Long id){
		if(null != id) {
			return classRepository.findByGroupId(Arrays.asList(id));
		}else {
			return getCurrentClasses();
		}
	}
	
	public Classes insert(ClassDTO c) throws Exception {
		Optional<GroupClass> group = groupClassRepository.findById(c.getGroupClassId());
		Optional<User> user = userRepository.findById(c.getOwnerId());
		if(!group.isPresent()) throw new Exception("Group not found!!");
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
}

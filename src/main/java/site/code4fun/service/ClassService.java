package site.code4fun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Organization;
import site.code4fun.entity.dto.ClassDTO;
import site.code4fun.repository.ClassRepository;
import site.code4fun.repository.GroupClassRepository;
import site.code4fun.repository.OrganizationRepository;

@Service
public class ClassService extends BaseService{

	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private GroupClassRepository groupClassRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Cacheable(cacheNames = "lstClass")
	public List<Classes> getByGroupId(Long id){
		List<Long> idsGroup = new ArrayList<>();
		if(null != id) {
			idsGroup.add(id);
		}else {
			List<Organization> lstOrganization = organizationRepository.findByUserId(getCurrentId());
			List<Long> idsOr = lstOrganization.stream().map(Organization::getId).collect(Collectors.toList());
			if(idsOr.size() < 0 ) return new ArrayList<>();
			List<GroupClass> lstGr = groupClassRepository.findByOrganizationIds(idsOr);
			idsGroup = lstGr.stream().map(GroupClass::getId).collect(Collectors.toList());
		}
		return idsGroup.size() > 0 ? classRepository.findByGroupId(idsGroup) : new ArrayList<>();
	}
	
	public Classes insert(ClassDTO c) throws Exception {
		Optional<GroupClass> group = groupClassRepository.findById(c.getGroupClassId());
		if(!group.isPresent()) throw new Exception("Group not found!!");
		Classes clazz = Classes.builder()
				.name(c.getName())
				.note(c.getNote())
				.time(c.getTime())
				.groupClass(group.get())
				.build();
		return classRepository.saveAndFlush(clazz);
	}
	
	public void delete(Long id) throws Exception {
		Optional<Classes> item = classRepository.findById(id);
		if (!item.isPresent()) throw new Exception("Class not found!");
		classRepository.deleteById(id);
	}
}

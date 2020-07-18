package site.code4fun.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Lession;
import site.code4fun.entity.Organization;
import site.code4fun.util.StringUtils;

@Service
public class LessionService extends BaseService{
	
	public List<Lession> getAll(){
		List<Organization> lstOrganization = organizationRepository.findByUserId(getCurrentId());
		List<Long> idsOr = lstOrganization.stream().map(Organization::getId).collect(Collectors.toList());
		
		if(idsOr.size() == 0 ) return new ArrayList<>();
		
		List<GroupClass> lstGr = groupClassRepository.findByOrganizationIds(idsOr);
		List<Long> idsGroup = lstGr.stream().map(GroupClass::getId).collect(Collectors.toList());
		List<Classes> lstClass = classRepository.findByGroupId(idsGroup);
		List<Long> idsClass = lstClass.stream().map(Classes::getId).collect(Collectors.toList());
		
		return jLessionRepository.findByClassIds(idsClass);
	}
	
	public List<Lession> getByClassId(Long id){
		return jLessionRepository.findByClassIds(Arrays.asList(id));
	}
	
	public Lession insert(Lession c) throws Exception {
		if(StringUtils.isNull(c.getTitle())) throw new Exception("Lession name is null!!");
		List<Long> idsClass = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		
		if(!idsClass.contains(c.getClassId())) throw new Exception("Class not found!!");
		
		return lessionRepository.saveAndFlush(c);
	}

	public boolean delete(Long id) {
		lessionRepository.deleteById(id);
		return true;
	}
}

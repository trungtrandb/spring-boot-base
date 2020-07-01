package site.code4fun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Lession;
import site.code4fun.entity.Organization;
import site.code4fun.repository.ClassRepository;
import site.code4fun.repository.GroupClassRepository;
import site.code4fun.repository.OrganizationRepository;
import site.code4fun.repository.LessionRepository;
import site.code4fun.repository.jdbc.JLessionRepository;
import site.code4fun.util.StringUtils;

@Service
public class LessionService extends BaseService{
	
	@Autowired
	private LessionRepository lessionRepository;
	
	@Autowired
	private JLessionRepository jLessionRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private GroupClassRepository groupClassRepository;
	
	@Autowired
	private ClassRepository classRepository;
	
	public List<Lession> getAll(int page, int limit){
		int offset = (page - 1) * limit;
		List<Organization> lstOrganization = organizationRepository.findByUserId(getCurrentId());
		List<Long> idsOr = lstOrganization.stream().map(Organization::getId).collect(Collectors.toList());
		
		if(idsOr.size() == 0 ) return new ArrayList<>();
		
		List<GroupClass> lstGr = groupClassRepository.findByOrganizationIds(idsOr);
		List<Long> idsGroup = lstGr.stream().map(GroupClass::getId).collect(Collectors.toList());
		List<Classes> lstClass = classRepository.findByGroupId(idsGroup);
		List<Long> idsClass = lstClass.stream().map(Classes::getId).collect(Collectors.toList());
		
		return jLessionRepository.findByClassIds(idsClass, limit, offset);
	}
	
	public Lession insert(Lession c) throws Exception {
		if(StringUtils.isNull(c.getTitle())) throw new Exception("Subject name is null!!");
		
		List<Organization> lstOrganization = organizationRepository.findByUserId(getCurrentId());
		List<Long> idsOr = lstOrganization.stream().map(Organization::getId).collect(Collectors.toList());
		
		if(idsOr.size() < 0 ) throw new Exception("Class not found!!");
		
		List<GroupClass> lstGr = groupClassRepository.findByOrganizationIds(idsOr);
		List<Long> idsGroup = lstGr.stream().map(GroupClass::getId).collect(Collectors.toList());
		List<Classes> lstClass = classRepository.findByGroupId(idsGroup);
		List<Long> idsClass = lstClass.stream().map(Classes::getId).collect(Collectors.toList());
		
		if(!idsClass.contains(c.getClassId())) throw new Exception("Class not found!!");
		
		return lessionRepository.saveAndFlush(c);
	}

	public boolean delete(Long id) {
		lessionRepository.deleteById(id);
		return true;
	}
}

package site.code4fun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Organization;
import site.code4fun.entity.UserPrincipal;
import site.code4fun.repository.ClassRepository;
import site.code4fun.repository.GroupClassRepository;
import site.code4fun.repository.OrganizationRepository;
import site.code4fun.repository.UserRepository;

@Service
public class BaseService {
	
	@Autowired
	protected OrganizationRepository organizationRepository;
	
	@Autowired
	protected GroupClassRepository groupClassRepository;
	
	@Autowired 
	protected ClassRepository classRepository;
	
	@Autowired 
	protected UserRepository userRepository;
	
	protected final Long getCurrentId() {
		UserPrincipal currentUser = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return currentUser.getId();
	}
	
	protected final List<Organization> getCurrentOrganization(){
		return organizationRepository.findByUserId(getCurrentId());
	}
	
	protected final List<GroupClass> getCurrentGroupClass(){
		List<Long> idsOrganization = getCurrentOrganization().stream().map(Organization::getId).collect(Collectors.toList());
		return idsOrganization.size() > 0 ? groupClassRepository.findByOrganizationIds(idsOrganization) : new ArrayList<>();
	}
	
	protected final List<Classes> getCurrentClasses(){
		List<Long> idsGroupClass = getCurrentGroupClass().stream().map(GroupClass::getId).collect(Collectors.toList());
		return idsGroupClass.size() > 0 ? classRepository.findByGroupId(idsGroupClass) : new ArrayList<>();
	}
} 

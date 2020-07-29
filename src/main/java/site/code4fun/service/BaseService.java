package site.code4fun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Organization;
import site.code4fun.entity.UserPrincipal;
import site.code4fun.repository.CheckinRepository;
import site.code4fun.repository.ClassRepository;
import site.code4fun.repository.GroupClassRepository;
import site.code4fun.repository.LessionRepository;
import site.code4fun.repository.NotifyDeviceRepository;
import site.code4fun.repository.NotifyRepository;
import site.code4fun.repository.OrganizationRepository;
import site.code4fun.repository.StudentRepository;
import site.code4fun.repository.SubjectRepository;
import site.code4fun.repository.UserDeviceRepository;
import site.code4fun.repository.UserOrganizationRepository;
import site.code4fun.repository.UserRepository;
import site.code4fun.repository.jdbc.JCheckinRepository;
import site.code4fun.repository.jdbc.JLessionRepository;
import site.code4fun.repository.jdbc.JNotifyRepository;
import site.code4fun.repository.jdbc.JStudentRepository;
import site.code4fun.repository.jdbc.JUserOrganizationRepository;
import site.code4fun.util.MailUtil;

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
	
	@Autowired
	protected StudentRepository studentRepository;

	@Autowired
	protected QueueService queueService;

	@Autowired
	protected ClassService classService;
	
	@Autowired
	protected LessionRepository lessionRepository;
	
	@Autowired
	protected UserOrganizationRepository userOrganizationRepository;
	
	@Autowired
	protected JStudentRepository jStudentRepository;
	
	@Autowired
	protected JUserOrganizationRepository jUserOrganizationRepository;
	
	@Autowired
	protected JLessionRepository jLessionRepository;
	
	@Autowired 
	protected MailUtil mailUtil;
	
	@Autowired
	protected NotifyRepository notifyRepository;
	
	@Autowired
	protected NotifyDeviceRepository notifyDeviceRepository;
	
	@Autowired
	protected CheckinRepository checkinRepository;
	
	@Autowired
	protected OrganizationRepository organizationRepsitory;
	
	@Autowired
	protected JCheckinRepository jCheckinRepository;

	@Autowired
	protected SubjectRepository subjectRepository;
	
	@Autowired
	protected UserDeviceRepository userDeviceRepository;
	
	@Autowired
	protected JNotifyRepository jNotifyRepository;

	
	protected final Long getCurrentId() {
		UserPrincipal currentUser = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return currentUser.getId();
	}
	
	protected final Organization getCurrentOrganization(){
		Optional<Organization> org = organizationRepository.findFirstByUserId(getCurrentId());
		return org.isPresent() ? org.get() : null;
	}
	
	protected final List<GroupClass> getCurrentGroupClass(){
		Organization org = getCurrentOrganization();
		return org != null ? groupClassRepository.findByOrganizationId(org.getId()) : new ArrayList<>();
	}
	
	protected final List<Classes> getCurrentClasses(){
		List<Long> idsGroupClass = getCurrentGroupClass().stream().map(GroupClass::getId).collect(Collectors.toList());
		return idsGroupClass.size() > 0 ? classRepository.findByGroupId(idsGroupClass) : new ArrayList<>();
	}
} 

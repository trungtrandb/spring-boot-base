package site.code4fun.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.constant.Status;
import site.code4fun.entity.Checkin;
import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Notify;
import site.code4fun.entity.NotifyDevice;
import site.code4fun.entity.Organization;
import site.code4fun.entity.Student;
import site.code4fun.entity.dto.CheckinDTO;
import site.code4fun.entity.dto.CheckinFilterDTO;
import site.code4fun.repository.CheckinRepository;
import site.code4fun.repository.ClassRepository;
import site.code4fun.repository.GroupClassRepository;
import site.code4fun.repository.NotifyRepository;
import site.code4fun.repository.OrganizationRepository;
import site.code4fun.repository.StudentRepository;
import site.code4fun.repository.jdbc.JCheckinRepository;
import site.code4fun.repository.jdbc.JStudentRepository;

@Service
public class CheckinService extends BaseService{
	
	@Autowired 
	private ClassService classService;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private JStudentRepository jStudentRepository;
	
	@Autowired
	private NotifyRepository notifyRepository;
	
	@Autowired
	private CheckinRepository checkinRepository;
	
	@Autowired
	private OrganizationRepository organizationRepsitory;
	
	@Autowired
	private GroupClassRepository groupClassRepository;
	
	@Autowired
	private ClassRepository classRepository;
	
	@Autowired
	private JCheckinRepository jCheckinRepository;
	
	public Checkin insert(Checkin check) throws Exception {
		if(null == check.getSubjectId()) throw new Exception("Subject not found!!");
		List<Long> idsClass = classService.getByGroupId(null).stream().map(Classes::getId).collect(Collectors.toList());
		if(!idsClass.contains(check.getClassId())) throw new Exception("Class permision denied!!");
		
		List<Long> idsStudent = studentRepository.findStudentByClassId(Arrays.asList(check.getClassId()))
				.stream().map(Student::getId).collect(Collectors.toList());
		
		if(!idsStudent.contains(check.getStudentId())) throw new Exception("Student not found!!");
		
		// Update if has checked in
		Optional<Checkin> checked = checkinRepository.checkExist(check.getStudentId(), check.getClassId(), check.getSubjectId());
		check.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		if(checked.isPresent()) check.setId(checked.get().getId());
		
		if (!check.isPresent()) {
			List<NotifyDevice> lstDevice = jStudentRepository.findParentDeviceByStudentId(check.getStudentId());
			String title = "Notify Checkin";
			StringBuilder notifyContent = new StringBuilder("Student is absent from class today!");
			
			List<Notify> lstNotify = new ArrayList<>();
			lstDevice.forEach(item ->{
				Notify noti = Notify.builder()
						.title(title)
						.deviceToken(item.getDeviceToken())
						.userId(item.getUserId())
						.content(notifyContent.toString())
						.status(Status.PENDING.getVal())
						.createdBy(getCurrentId())
						.createdDate(new Timestamp(System.currentTimeMillis()))
						.build();
				lstNotify.add(noti);		
			});
			notifyRepository.saveAll(lstNotify);
		}
		
		check.setCreatedBy(getCurrentId());
		return checkinRepository.saveAndFlush(check);
	}

	public List<CheckinDTO> getAll(CheckinFilterDTO filter) {
		Map<Long, String> mapOrganization = new HashMap<>();
		if(filter.getOrganizationId() != null) {
			Optional<Organization> orga = organizationRepsitory.findById(filter.getOrganizationId());
			if(orga.isPresent()) mapOrganization.put(orga.get().getId(), orga.get().getName());
		}else {
			mapOrganization = organizationRepsitory.findByUserId(getCurrentId())
					.stream().collect(Collectors.toMap(Organization::getId, Organization::getName));
		}
		
		Map<Long, String> mapGroup = groupClassRepository.findByOrganizationIds(new ArrayList<>(mapOrganization.keySet()))
				.stream().collect(Collectors.toMap(GroupClass::getId, GroupClass::getName));
		
		Map<Long, String> mapClass = new HashMap<>();
		if(filter.getClassId() != null) {
			Optional<Classes> clazz = classRepository.findById(filter.getClassId());
			if(clazz.isPresent()) mapClass.put(clazz.get().getId(), clazz.get().getName());
		}else {
			if(mapGroup.keySet().size() == 0) return new ArrayList<>();
			mapClass = classRepository.findByGroupId(new ArrayList<>(mapGroup.keySet()))
					.stream().collect(Collectors.toMap(Classes::getId, Classes::getName));
		}
		
		List<CheckinDTO> lstRes = jCheckinRepository.getAllCheckin(filter.getSubjectId(), new ArrayList<>(mapClass.keySet()), filter.getCreatedDate());

		for(CheckinDTO _item : lstRes) {
			if(mapClass.containsKey(_item.getClassId())) _item.setClassName(mapClass.get(_item.getClassId()));
		}
		return lstRes;
	}
}

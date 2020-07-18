package site.code4fun.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.code4fun.constant.Status;
import site.code4fun.entity.Checkin;
import site.code4fun.entity.Classes;
import site.code4fun.entity.GroupClass;
import site.code4fun.entity.Lession;
import site.code4fun.entity.Notify;
import site.code4fun.entity.NotifyDevice;
import site.code4fun.entity.UserDevice;
import site.code4fun.entity.Organization;
import site.code4fun.entity.Student;
import site.code4fun.entity.dto.CheckinDTO;
import site.code4fun.entity.dto.CheckinFilterDTO;

@Service
public class CheckinService extends BaseService{
	
	public Checkin insert(Checkin check) throws Exception {
		if(null == check.getLessionId()) throw new Exception("Chưa chọn buổi học!");
		List<Long> idsClass = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		if(!idsClass.contains(check.getClassId())) throw new Exception("Không tìm thấy lớp học!");
		
		Optional<Student> student = studentRepository.findById(check.getStudentId());
		if(!student.isPresent() || student.get().getClassId() != check.getClassId()) throw new Exception("Không tìm thấy học sinh!");
		
		Optional<Lession> lession = lessionRepository.findById(check.getLessionId());
		if(!lession.isPresent() || lession.get().getClassId() != check.getClassId()) throw new Exception("Không tìm thấy buổi học!");
		
		// Update nếu điểm danh lại
		Optional<Checkin> checked = checkinRepository.checkExist(check.getStudentId(), check.getClassId(), check.getLessionId());
		if(checked.isPresent()) check.setId(checked.get().getId());
		
		if (!check.isPresent()) { // Vắng mặt
			List<UserDevice> lstDevice = jStudentRepository.findParentDeviceByStudentId(check.getStudentId());
			if(lstDevice.size() > 0) {
				String title = "Thông báo điểm danh";
				StringBuilder notifyContent = new StringBuilder(student.get().getName() + "đã điểm danh vắng trong buổi học " + lession.get().getTitle() + "!");
				Notify noti = Notify.builder()
						.title(title)
						.content(notifyContent.toString())
						.status(Status.PENDING.getVal())
						.createdBy(getCurrentId())
						.createdDate(new Timestamp(System.currentTimeMillis()))
						.build();
				noti = notifyRepository.saveAndFlush(noti);
				
				List<NotifyDevice> lstNotifyDevice = new ArrayList<>();
				for(UserDevice _item : lstDevice) {
					NotifyDevice notiDevice = NotifyDevice.builder()
							.deviceToken(_item.getDeviceToken())
							.notifyId(noti.getId())
							.createdDate(new Timestamp(System.currentTimeMillis()))
							.createdBy(noti.getCreatedBy())
							.status(Status.PENDING.getVal())
							.build();
					lstNotifyDevice.add(notiDevice);
				};
				notifyDeviceRepository.saveAll(lstNotifyDevice);	
			}
		}
		
		check.setCreatedDate(new Timestamp(System.currentTimeMillis()));
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
		if(mapOrganization.size() == 0 ) return new ArrayList<>();
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
		
		List<CheckinDTO> lstRes = jCheckinRepository.getAllCheckin(filter.getLessionId(), new ArrayList<>(mapClass.keySet()), filter.getCreatedDate());

		for(CheckinDTO _item : lstRes) {
			if(mapClass.containsKey(_item.getClassId())) _item.setClassName(mapClass.get(_item.getClassId()));
		}
		return lstRes;
	}
}

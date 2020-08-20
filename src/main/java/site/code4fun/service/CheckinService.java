package site.code4fun.service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import site.code4fun.constant.Status;
import site.code4fun.entity.Checkin;
import site.code4fun.entity.Classes;
import site.code4fun.entity.Lession;
import site.code4fun.entity.Notify;
import site.code4fun.entity.NotifyDevice;
import site.code4fun.entity.UserDevice;
import site.code4fun.entity.Student;
import site.code4fun.entity.dto.CheckinDTO;
import site.code4fun.entity.dto.CheckinFilterDTO;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.util.StringUtils;

@Service
public class CheckinService extends BaseService{
	
	public Checkin insert(Checkin check) throws Exception {
		if(null == check.getLessionId()) throw new Exception("Chưa chọn buổi học!");
		List<Long> idsClass = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		if(!idsClass.contains(check.getClassId())) throw new Exception("Không tìm thấy lớp học!");
		
		Optional<Student> student = studentRepository.findById(check.getStudentId());
		if(!student.isPresent() || !student.get().getClassId().equals(check.getClassId())) throw new Exception("Không tìm thấy học sinh!");
		
		Optional<Lession> lession = lessionRepository.findById(check.getLessionId());
		if(!lession.isPresent() || !lession.get().getClassId().equals(check.getClassId())) throw new Exception("Không tìm thấy buổi học!");
		
		// Update nếu điểm danh lại
		Optional<Checkin> checked = checkinRepository.checkExist(check.getStudentId(), check.getLessionId());
		checked.ifPresent(checkin -> check.setId(checkin.getId()));
		
		if (!check.getPresent()) { // Vắng mặt
			new Thread(() -> {
				List<UserDevice> lstDevice = jStudentRepository.findParentDeviceByStudentId(check.getStudentId());
				if(lstDevice.size() > 0) {
					String title = "Thông báo điểm danh";
					Notify noti = Notify.builder()
							.title(title)
							.content(student.get().getName() + "đã điểm danh vắng trong buổi học " + lession.get().getTitle() + "!")
							.status(Status.PENDING)
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
								.status(Status.PENDING)
								.userId(_item.getUserId())
								.isRead(false)
								.build();
						lstNotifyDevice.add(notiDevice);
					}
					notifyDeviceRepository.saveAll(lstNotifyDevice);
				}
			}).start();
		}
		
		check.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		check.setCreatedBy(getCurrentId());
		return checkinRepository.saveAndFlush(check);
	}

	public List<CheckinDTO> getAll(CheckinFilterDTO filter) {
		List<Long> classIds = new ArrayList<>();
		if(filter.getClassId() != null) {
			Optional<Classes> clazz = classRepository.findById(filter.getClassId());
			if(clazz.isPresent()) classIds.add(clazz.get().getId());
		}else {
			classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		}
		List<StudentDTO> lstStudent = jStudentRepository.findByClassId(classIds, null);
		List<Long> studentIds = lstStudent.stream().map(StudentDTO::getId).collect(Collectors.toList());

		return jCheckinRepository.getAllCheckin(filter.getLessionId(), studentIds, filter.getCreatedDate());
	}

	public Collection<CheckinDTO> getCheckin(Checkin c){
		Timestamp createdDate = c.getCreatedDate() != null ? c.getCreatedDate() : new Timestamp(System.currentTimeMillis());
		List<StudentDTO> lstStudent = jStudentRepository.findByClassId(Collections.singletonList(c.getClassId()), null);
		List<Long> studentIds = lstStudent.stream().map(StudentDTO::getId).collect(Collectors.toList());
		String studentIdString = StringUtils.stringFromList(studentIds);
		Optional<Classes> cl = classRepository.findById(c.getClassId());
		List<CheckinDTO> lstCheckinDTO = jCheckinRepository.getDetailCheckin(studentIdString, c.getLessionId(), createdDate);
		Map<Long, CheckinDTO> mapRes = lstCheckinDTO.stream().collect(Collectors.toMap(CheckinDTO::getStudentId, _c -> _c));
		lstStudent.forEach(_student ->{
			if (!mapRes.containsKey(_student.getId())){
				CheckinDTO dto = CheckinDTO.builder()
						.studentId(_student.getId())
						.studentName(_student.getName())
						.studentCode(_student.getStudentCode())
						.classId(c.getClassId())
						.className(cl.isPresent() ? cl.get().getName() : "")
						.build();
				mapRes.put(_student.getId(), dto);
			}
		});
		return mapRes.values();
	}
}

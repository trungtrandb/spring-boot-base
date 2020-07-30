package site.code4fun.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import site.code4fun.constant.Queue;
import site.code4fun.constant.Status;
import site.code4fun.entity.Classes;
import site.code4fun.entity.Notify;
import site.code4fun.entity.NotifyDevice;
import site.code4fun.entity.Organization;
import site.code4fun.entity.UserDevice;
import site.code4fun.entity.dto.NotifyDTO;
import site.code4fun.util.FirebaseAdmin;
import site.code4fun.util.StringUtils;

@Service
public class NotifyService extends BaseService{
	
	/*
	 * Every 1'
	 * Run at 21h00 every day: "0 21 * * *"
	*/
	@Scheduled(cron = "0 */1 * * * *")
	private void schedulePushNotify() {
	    List<NotifyDTO> lstNoti = jNotifyRepository.getNotifyOfDevicePending();
	    Gson gson = new Gson();
	    lstNoti.forEach(_noti -> {
	    	String mess = gson.toJson(_noti);
	    	queueService.sendToQueue(Queue.QUEUE_NOTIFY, mess);
	    });
	}
	
	@JmsListener(destination = "notify.queue")
    private void pushNotifyFromQueue(final javax.jms.Message jsonMessage) throws JMSException {
        if(jsonMessage instanceof TextMessage) {
        	TextMessage textMessage = (TextMessage)jsonMessage;
        	NotifyDTO noti = new Gson().fromJson(textMessage.getText(), NotifyDTO.class);
        	NotifyDevice notiDevice = NotifyDevice.builder().notifyId(noti.getId()).deviceToken(noti.getDeviceToken()).build();
            try {
				FirebaseAdmin.pushNotification(noti.getTitle(), noti.getContent(), noti.getDeviceToken());
				notiDevice.setStatus(Status.COMPLETE);
			} catch (Exception e) {
				notiDevice.setNote(e.getMessage());
				notiDevice.setStatus(Status.ERROR);
				e.printStackTrace();
			}
            jNotifyRepository.updateNoti(notiDevice);
        }
    }
	
	public Notify create(Notify item) throws Exception {
		if(StringUtils.isNull(item.getTitle())) throw new Exception("Tiều đề không được bỏ trống!");
		if(StringUtils.isNull(item.getContent())) throw new Exception("Nội dung thông báo không được bỏ trống!");
		Organization org = getCurrentOrganization();
		if(null == org) throw new Exception("Chưa có trường.");
		if(item.getStatus() == null) item.setStatus(Status.DRAFT);
		item.setCreatedBy(getCurrentId());
		item.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		item.setSchoolId(org.getId());
		item = notifyRepository.save(item);
		List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		if(Status.ACTIVE.equalsIgnoreCase(item.getStatus())){
			List<UserDevice> lst = userDeviceRepository.getDeviceByClassIds(StringUtils.stringFromList(classIds));
			List<NotifyDevice> lstNotify = new ArrayList<>();
			for(UserDevice _device : lst){
				NotifyDevice nt = NotifyDevice.builder()
						.createdBy(getCurrentId())
						.deviceToken(_device.getDeviceToken())
						.notifyId(item.getId())
						.userId(_device.getUserId())
						.createdDate(new Timestamp(System.currentTimeMillis()))
						.status(Status.PENDING)
						.isRead(false)
						.build();
				lstNotify.add(nt);
			}
			notifyDeviceRepository.saveAll(lstNotify);
		}
		return item;
	}
	
	public List<NotifyDTO> getAll(){
		Organization org = getCurrentOrganization();
		return null != org ? jNotifyRepository.getNotifyByOrg(org.getId()) : new ArrayList<>();
	}
	
	public Optional<Notify> getById(Long id) {
		return notifyRepository.findById(id);
	}
	
	public List<NotifyDTO> getByUser(){
		return jNotifyRepository.getNotifyByUserId(getCurrentId());
	}
	
	public boolean delete(Long id) {
		List<NotifyDevice> lst = notifyDeviceRepository.findByNotifyId(id);
		if(lst.size() == 0) {
			notifyRepository.deleteById(id);
			return true;
		}
		return false;
	}
	
	public boolean isRead(List<Long> notifyIds) {
		return true;
	}
}

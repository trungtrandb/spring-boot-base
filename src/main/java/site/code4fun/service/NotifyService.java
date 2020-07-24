package site.code4fun.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
				notiDevice.setStatus(Status.COMPLETE.getVal());
			} catch (Exception e) {
				notiDevice.setNote(e.getMessage());
				notiDevice.setStatus(Status.ERROR.getVal());
				e.printStackTrace();
			}
            jNotifyRepository.updateNoti(notiDevice);
        }
    }
	
	public NotifyDTO create(NotifyDTO item) throws Exception {
		if(StringUtils.isNull(item.getTitle())) throw new Exception("Tiều đề không được bỏ trống!");
		if(StringUtils.isNull(item.getContent())) throw new Exception("Nội dung thông báo không được bỏ trống!");
		List<Long> classIds = getCurrentClasses().stream().map(Classes::getId).collect(Collectors.toList());
		if (item.getClassIds() !=  null && item.getClassIds().size() > 0) {
			item.getClassIds().removeIf(id -> !classIds.contains(id));
			classIds.removeIf(id -> !item.getClassIds().contains(id));
		}
		
		item.setCreatedBy(getCurrentId());
		item.setCreatedDate(new Timestamp(System.currentTimeMillis()));
//		List<Notify> lstNotify = new ArrayList<>();
//		classIds.forEach(_item -> {
//			item.setClassId(_item);
//			lstNotify.add(item);
//		});

		if(item.getStatus().equalsIgnoreCase(Status.ACTIVE.getVal())){
			List<UserDevice> lst = userDeviceRepository.getDeviceByClassIds(classIds);
			lst.forEach(_device ->{
//				noti
			});
		}
		return null;
	}
}

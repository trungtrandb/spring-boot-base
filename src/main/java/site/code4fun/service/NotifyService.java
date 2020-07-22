package site.code4fun.service;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import site.code4fun.constant.Queue;
import site.code4fun.constant.Status;
import site.code4fun.entity.NotifyDevice;
import site.code4fun.entity.dto.NotifyDTO;
import site.code4fun.repository.jdbc.JNotifyRepository;
import site.code4fun.util.FirebaseAdmin;

/**
 * @author Trung
 *
 */
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
}

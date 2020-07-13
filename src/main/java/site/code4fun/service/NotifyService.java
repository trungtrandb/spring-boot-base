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
import site.code4fun.entity.Notify;
import site.code4fun.repository.NotifyRepository;
import site.code4fun.util.FirebaseAdmin;

/**
 * @author Trung
 *
 */
@Service
public class NotifyService {
	
	@Autowired
	NotifyRepository notifyRepository;
	
	@Autowired
	private QueueService queueService;

	/*
	 * Every 5'
	 * Run at 21h00 every day: "0 21 * * *"
	*/
	@Scheduled(cron = "0 */5 * * * *")
	public void schedulePushNotify() {
	    List<Notify> lstNoti = notifyRepository.findByStatus(Status.PENDING.getVal());
	    Gson gson = new Gson();
	    lstNoti.forEach(_noti -> {
	    	String mess = gson.toJson(_noti);
	    	queueService.sendToQueue(Queue.QUEUE_NOTIFY, mess);
	    });
	}
	
//	@JmsListener(destination = "notify.queue")
//    private void pushNotifyFromQueue(final javax.jms.Message jsonMessage) throws JMSException {
//        if(jsonMessage instanceof TextMessage) {
//        	TextMessage textMessage = (TextMessage)jsonMessage;
//			Notify noti = new Gson().fromJson(textMessage.getText(), Notify.class);
//            try {
//				FirebaseAdmin.pushNotification(noti.getTitle(), noti.getContent(), noti.getDeviceToken());
//				noti.setStatus(Status.COMPLETE.getVal());
//			} catch (Exception e) {
//				noti.setStatus(Status.ERROR.getVal());
//				noti.setNote(e.getMessage());
//				e.printStackTrace();
//			}
//            notifyRepository.saveAndFlush(noti);
//        }
//    }

}

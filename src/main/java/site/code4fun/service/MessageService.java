package site.code4fun.service;

import java.sql.Timestamp;
import org.springframework.stereotype.Service;

import site.code4fun.constant.Status;
import site.code4fun.entity.Message;

@Service
public class MessageService extends BaseService{
	
	public Message sendDirect(Message mess) {
		mess.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		mess.setStatus(Status.PENDING);
		return messageRepository.save(mess);
//		queueService.sendToQueue(Queue.QUEUE_MESSAGE,new Gson().toJson(mess));
	}
	
//	@JmsListener(destination = Queue.QUEUE_MESSAGE)
//    public void pushNotifyFromQueue(final javax.jms.Message jsonMessage) throws JMSException {
//        if(jsonMessage instanceof TextMessage) {
//        	TextMessage textMessage = (TextMessage)jsonMessage;
//        	Message mess = new Gson().fromJson(textMessage.getText(), Message.class);
//            messageRepository.save(mess);
//        }
//    }
}

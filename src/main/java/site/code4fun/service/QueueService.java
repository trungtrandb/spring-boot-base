package site.code4fun.service;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import site.code4fun.constant.Queue;

@Service
public class QueueService {
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	public void sendToQueue(Queue queueName, String message) {
        jmsTemplate.send(queueName.getVal(), new MessageCreator() {
            public javax.jms.Message createMessage(Session session) throws JMSException {
            	TextMessage txtMessage = session.createTextMessage(message);
            	txtMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 60 * 1000);
//            	message.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, "0 * * * *");
                return txtMessage;
            }
        });
    }
}

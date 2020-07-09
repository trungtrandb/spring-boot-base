package site.code4fun.util;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;


@Component
public class MailUtil{
	
	@Autowired
    public JavaMailSender emailSender;
 
    public void sendmail(String receiverAddress, String mailSubject, String content) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(receiverAddress); 
        message.setSubject(mailSubject); 
        message.setText(content);
        emailSender.send(message);
    }

	@JmsListener(destination = "mail.queue")
    private void sendMail(final javax.jms.Message jsonMessage) throws JMSException {
        if(jsonMessage instanceof TextMessage) {
            try {
            	TextMessage textMessage = (TextMessage)jsonMessage;
                String messageData = textMessage.getText();
                
                @SuppressWarnings("unchecked")
				Map<String, String> map = new Gson().fromJson(messageData, Map.class);
				sendmail(map.get("receiver"), map.get("subject"), map.get("content"));
			} catch (Exception e) {
				e.printStackTrace();
				sendmail("trungtrandb@gmail.com", "Lỗi send mail Edu", e.getMessage());
			}
        }
    }
}

package site.code4fun.util;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.util.Map;


@Component
public class MailUtil{

    private final JavaMailSender emailSender;

	@Autowired
    private MailUtil(JavaMailSender mailSender){
        this.emailSender = mailSender;
    }
 
    public void sendmail(String receiverAddress, String mailSubject, String content) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(receiverAddress); 
        message.setSubject(mailSubject); 
        message.setText(content);
        emailSender.send(message);
    }

	@JmsListener(destination = "mail.queue")
    private void sendMailFromQueue(final Message jsonMessage){
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

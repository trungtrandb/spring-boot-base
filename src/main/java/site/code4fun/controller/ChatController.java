package site.code4fun.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import site.code4fun.entity.Message;
import site.code4fun.entity.OutputMessage;
import site.code4fun.entity.User;
import site.code4fun.entity.UserPrincipal;
import site.code4fun.service.UserService;

@Controller
public class ChatController {
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private UserService userService;
	
	@MessageMapping("/topic/chat")
    @SendTo("/topic/message")
    public OutputMessage sendAll(@Payload Message msg, Authentication auth) throws Exception {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		User user = userService.getByUserName(principal.getUsername());
		
		OutputMessage out = OutputMessage.builder()
	    		.from(user.getUsername())
	    		.avatar(user.getAvatar())
	    		.fullName(user.getFullName())
	    		.text(msg.getText())
	    		.time(new SimpleDateFormat("HH:mm").format(new Date()))
	    		.build();
        return out;
    }
	
	@MessageMapping("/secured/chat") 
	public void sendSpecific(@Payload Message msg, Principal user) throws Exception { 		
	    OutputMessage out = OutputMessage.builder()
	    		.from(msg.getFrom())
	    		.text(msg.getText())
	    		.time(new SimpleDateFormat("HH:mm").format(new Date()))
	    		.build();
	    simpMessagingTemplate.convertAndSendToUser(msg.getTo(), "/secured/user/specific-user", out); 
	}
}

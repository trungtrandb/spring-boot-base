package site.code4fun.controller;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import site.code4fun.entity.Message;
import site.code4fun.entity.OutputMessage;
import site.code4fun.entity.User;
import site.code4fun.entity.UserPrincipal;
import site.code4fun.service.MessageService;
import site.code4fun.service.UserService;

@Controller
public class ChatController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;

	@MessageMapping("/topic/chat")
//    @SendTo("/topic/message")
	public void sendAll(@Payload Message msg, Authentication auth) throws Exception {
		//UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		//User user = userService.getByUserName(principal.getUsername());

		simpMessagingTemplate.convertAndSend("/topic/message", msg);
	}
	
	@MessageMapping("/esp/send")
	public void sendFromEsp(@Payload Message msg) throws Exception {
		System.out.println(msg);
	}

	@MessageMapping("/direct/chat")
	public void sendDirect(@Payload Message msg, Authentication auth) {
		System.out.print(msg);
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		User user = userService.getByUserName(principal.getUsername());
		
		msg.setFrom(user.getUsername());
		msg.setFromId(user.getId());
		msg.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		msg = messageService.sendDirect(msg);
		
		OutputMessage out = OutputMessage.builder()
				.from(msg.getFrom())
				.to(msg.getTo())
				.fromId(msg.getFromId())
				.createdDate(msg.getCreatedDate())
				.text(msg.getText())
				.id(msg.getId())
				.build();
		
		out.setFullName(user.getFullName());
		out.setAvatar(user.getAvatar());
		simpMessagingTemplate.convertAndSendToUser(msg.getTo(), "/queue/reply", out);
	}
}

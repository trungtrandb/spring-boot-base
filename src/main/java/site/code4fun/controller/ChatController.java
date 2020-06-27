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
import site.code4fun.service.UserService;

@Controller
public class ChatController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private UserService userService;

	@MessageMapping("/topic/chat")
//    @SendTo("/topic/message")
	public void sendAll(@Payload Message msg, Authentication auth) throws Exception {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
		User user = userService.getByUserName(principal.getUsername());

		OutputMessage out = OutputMessage.builder().from(user.getId()).avatar(user.getAvatar())
				.fullName(user.getFullName()).text(msg.getText()).time(new Timestamp(System.currentTimeMillis()))
				.build();
		simpMessagingTemplate.convertAndSend("/topic/message", out);
	}

	@MessageMapping("/direct/chat")
	public void sendDirect(@Payload Message msg, Authentication auth) {
		UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

		User user = userService.getByUserName(principal.getUsername());

		OutputMessage out = OutputMessage.builder().from(user.getId()).avatar(user.getAvatar())
				.fullName(user.getFullName()).text(msg.getText()).time(new Timestamp(System.currentTimeMillis()))
				.build();
		simpMessagingTemplate.convertAndSendToUser(msg.getTo(), "/queue/reply", out);
	}
}

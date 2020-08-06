package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import site.code4fun.entity.Response;
import site.code4fun.entity.User;
import site.code4fun.service.StudentService;
import site.code4fun.service.UserService;

@Controller
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	StudentService studentService;
	
	@RequestMapping(value = "/getInfo", method = RequestMethod.GET )
	public ResponseEntity<?> getById(){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", userService.getUserInfo()));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST )
	public ResponseEntity<?> updateUser(@RequestBody User user){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", userService.updateUser(user)));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/get-list-student", method = RequestMethod.GET ) // Danh sách con của phụ huynh
	public ResponseEntity<?> getListStudent(){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", studentService.getByCurrentParent()));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/get-list-conversion", method = RequestMethod.GET) // Danh sách hội thoại và last Message
	public ResponseEntity<?> getListConversion(){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", userService.getListConversion()));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/get-message-with-user", method = RequestMethod.GET) // Danh sách hội thoại và last Message
	public ResponseEntity<?> getMessageWithUser(@RequestParam String userName){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", userService.getMessageWithUser(userName)));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
}

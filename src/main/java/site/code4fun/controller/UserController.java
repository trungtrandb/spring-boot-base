package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import site.code4fun.constant.ResponseMessage;
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
			return ResponseEntity.ok(new Response(200, ResponseMessage.UPDATE_SUCCESS, userService.updateUser(user)));
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
	
	@RequestMapping(value = "/get-list-choose-student", method = RequestMethod.GET )
	public ResponseEntity<?> getListChooseStudent(){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", studentService.getListChooseSt()));
		}catch(Exception e) {
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/get-list-conversion", method = RequestMethod.GET) // Danh sách hội thoại và last Message
	public ResponseEntity<?> getListConversion(@RequestParam(required = false) String user){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", userService.getListConversion(user)));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/get-message-with-user/{userName}", method = RequestMethod.GET) // Danh sách hội thoại và last Message
	public ResponseEntity<?> getMessageWithUser(@PathVariable String userName, 
			@RequestParam(required = false)Long page,
			@RequestParam(required = false)Integer size ){
		try {
			return ResponseEntity.ok(new Response(200, "Successful", userService.getMessage(userName, page, size)));
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
}

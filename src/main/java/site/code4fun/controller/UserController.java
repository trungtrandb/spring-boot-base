package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import site.code4fun.Response;
import site.code4fun.entity.User;
import site.code4fun.service.UserService;

@Controller
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
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
	

}

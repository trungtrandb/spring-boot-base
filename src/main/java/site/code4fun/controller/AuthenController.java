package site.code4fun.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import site.code4fun.Response;
import site.code4fun.entity.SignInRequest;
import site.code4fun.entity.User;
import site.code4fun.entity.UserPrincipal;
import site.code4fun.service.NotifyDeviceService;
import site.code4fun.service.UserService;
import site.code4fun.util.JwtTokenUtil;

@Controller
public class AuthenController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotifyDeviceService notifyDeviceService;
	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody SignInRequest authenticationRequest) throws Exception {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		if(authenticationRequest.getDeviceToken() != null) notifyDeviceService.insert(authenticationRequest.getDeviceToken());
		return ResponseEntity.ok(jwtTokenUtil.generateToken(userPrincipal));
	}
	
	@RequestMapping(value = "/sign-up", method = RequestMethod.POST)
	public ResponseEntity<?> signUp(@RequestBody User user){
		try {
			return ResponseEntity.ok(new Response(200, "Success", userService.create(user)));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
	
	@RequestMapping(value = "/upload-image", method = RequestMethod.POST)
	public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
		String rootPath = servletContext.getRealPath("");
		String imagePath = "/resources/image/" + file.getOriginalFilename();
		Path path = Paths.get(rootPath + imagePath);

		try {
			Files.write(path, file.getBytes(), StandardOpenOption.CREATE);
			return ResponseEntity.ok(new Response(200, "Success", imagePath));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.ok(new Response(500, e.getMessage(), null));
		}
	}
}

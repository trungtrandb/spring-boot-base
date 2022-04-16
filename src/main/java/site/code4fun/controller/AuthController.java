package site.code4fun.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import site.code4fun.entity.SignInRequest;
import site.code4fun.entity.UserPrincipal;
import site.code4fun.entity.dto.AccessTokenResponseDTO;
import site.code4fun.entity.dto.ResponseDTO;
import site.code4fun.service.UserService;
import site.code4fun.util.JwtTokenUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@RestController
@Slf4j
public class AuthController {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenUtil jwtTokenUtil;

	private final UserService userService;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager,
						  JwtTokenUtil jwtTokenUtil,
						  UserService userService){
		this.authenticationManager = authenticationManager;
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
	}
	
	@PostMapping(value = "/login")
	public ResponseEntity<AccessTokenResponseDTO> createAuthenticationToken(@RequestBody SignInRequest authenticationRequest){
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return ResponseEntity.ok(jwtTokenUtil.generateToken(userPrincipal));
	}
	
	@PostMapping(value = "/upload-image")
	public ResponseEntity<ResponseDTO> saveFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
		String rootPath = servletContext.getRealPath("");
		String imagePath = "/resources/image/" + file.getOriginalFilename();
		Path path = Paths.get(rootPath + imagePath);

		try {
			Files.write(path, file.getBytes(), StandardOpenOption.CREATE);
			return ResponseEntity.ok(new ResponseDTO(200, imagePath));
		} catch (IOException e) {
			log.error(e.getMessage());
			return ResponseEntity.ok(new ResponseDTO(500, e.getMessage()));
		}
	}
}

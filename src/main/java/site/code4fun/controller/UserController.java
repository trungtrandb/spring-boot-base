package site.code4fun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.code4fun.entity.User;
import site.code4fun.entity.dto.ResponseDTO;
import site.code4fun.repository.UserRepository;

import java.util.Collection;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserRepository repository;

	@Autowired
	public UserController(UserRepository userRepository){
		this.repository = userRepository;
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseDTO> getById(@PathVariable Integer id){
		return ResponseEntity.ok().build();
	}

	@GetMapping("/")
	public Collection<User> findBooks() {
		return repository.findAll();
	}
}

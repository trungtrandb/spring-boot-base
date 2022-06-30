package site.code4fun.controller;

import com.googlecode.jmapper.JMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.code4fun.dto.UserDTO;
import site.code4fun.dto.request.UserRequest;
import site.code4fun.model.Role;
import site.code4fun.model.User;
import site.code4fun.service.UserService;

import javax.validation.Valid;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
	private final UserService userService;

	private final ModelMapper modelMapper;

	@Autowired
	public UserController(UserService userService,
						  ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
		this.userService = userService;
	}

	@GetMapping()
	public Page<UserDTO> getAllRolePaging(@RequestParam(required = false, defaultValue = "0") int page,
									   @RequestParam(required = false, defaultValue = "0") int _limit,
									   @RequestParam(required = false) String sort,
									   @RequestParam(required = false) String sortDir,
									   @RequestParam(required = false, defaultValue = "") String q_like) {
		return userService.getPaging(page, _limit, sortDir, sort, q_like).map(this::convertToDto);
	}

	@GetMapping("/profile")
	public UserDTO getCurrentUserProfile(){
		return modelMapper.map(userService.getCurrentUser(), UserDTO.class);
	}


	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public UserDTO getUserById(@PathVariable long id){
		return convertToDto(userService.getById(id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public UserDTO deleteUserById(@PathVariable long id){
		return new UserDTO();
	}

	@PostMapping
	public UserDTO createUser(@RequestBody @Valid UserRequest request){
		User u = userService.create(convertRequestToEntity(request));
		return convertToDto(u);
	}

	private UserDTO convertToDto(User source) {
		JMapper<UserDTO, User> userMapper = new JMapper<>(UserDTO.class, User.class);
		return userMapper.getDestination(source);
	}

	private User convertRequestToEntity(UserRequest source) {
		User u = modelMapper.map(source, User.class);
		if (isNotEmpty(source.getRoles())){
			u.setRoles(source.getRoles().stream().map(Role::new).collect(Collectors.toSet()));
		}
		return u;
	}
}

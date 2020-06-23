package site.code4fun.service;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import site.code4fun.constant.Role;
import site.code4fun.constant.Status;
import site.code4fun.entity.User;
import site.code4fun.repository.UserRepository;

@Service
public class UserService extends BaseService{

	@Autowired UserRepository userRepository;
	
	public User create(User u) throws Exception {
		User existUser = userRepository.findByUserName(u.getUsername());
		if(null != existUser) 
			throw new Exception("Username Exist!");
		
		if(null == u.getPassword() || null == u.getRePass() || !u.getPassword().equals(u.getRePass())) 
			throw new Exception("Password not match!");
		
		u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
		u.setStatus(Status.ACTIVE);
		u.setRole(Role.ROLE_USER.getVal());
		u.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		return userRepository.saveAndFlush(u);
	}

	public User getUserInfo() {
		return userRepository.findById(getCurrentId()).get();
	}

	public User updateUser(User u) throws Exception {
		if(null != u.getPassword() && !u.getPassword().equals(u.getRePass())) 
			throw new Exception("Password not match!");
		
		Optional<User> optUser = userRepository.findById(getCurrentId());
		if(!optUser.isPresent()) throw new Exception("User not found!");
		
		User user = optUser.get();
		user.setAddress(u.getAddress());
		user.setAvatar(u.getAvatar());
		user.setEmail(u.getEmail());
		user.setPhone(u.getPhone());
		user.setFullName(u.getFullName());
		user.setIdentityCard(u.getIdentityCard());
		user.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		if(null != u.getPassword()) {
			user.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
		}
		
		return userRepository.saveAndFlush(user);
	}
}

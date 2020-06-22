package site.code4fun.service;

import java.sql.Timestamp;

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
}

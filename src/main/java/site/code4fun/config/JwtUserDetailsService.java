package site.code4fun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import site.code4fun.mapper.UserMapper;
import site.code4fun.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		site.code4fun.entity.User user = userRepository.findByUserName(userName);
		if(user == null) throw new UsernameNotFoundException(userName);
		return UserMapper.userToUserPrinciple(user);
	}
}

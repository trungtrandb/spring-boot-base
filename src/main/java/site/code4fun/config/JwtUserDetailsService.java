package site.code4fun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.code4fun.constant.Status;
import site.code4fun.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;

	@Autowired
	public JwtUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		site.code4fun.model.User user = userRepository.findByUsername(userName);
		if(user == null) throw new UsernameNotFoundException(userName);
		if(Status.PENDING == user.getStatus()) {
			user.setStatus(Status.ACTIVE);
			userRepository.save(user);
		}
		return user;
	}
}

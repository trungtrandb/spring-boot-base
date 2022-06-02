package site.code4fun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.code4fun.constant.Status;
import site.code4fun.model.Role;
import site.code4fun.dto.UserPrincipal;
import site.code4fun.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService{

	private final UserRepository userRepository;

	@Autowired
	public JwtUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		site.code4fun.model.User user = userRepository.findByUserName(userName);
		if(user == null) throw new UsernameNotFoundException(userName);
		if(Status.PENDING == user.getStatus()) {
			user.setStatus(Status.ACTIVE);
			userRepository.save(user);
		}

		return UserPrincipal.builder()
				.id(user.getId())
				.fullName(user.getFullName())
				.password(user.getPassword())
				.roles(getRoles(user.getRoles()))
				.authorities(getAuthorities(user.getRoles()))
				.isAccountNonLocked(true)
				.isCredentialsNonExpired(true)
				.isAccountNonExpired(true)
				.isEnabled(true)
				.build();
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(role -> {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			role.getPrivileges().forEach(privilege -> authorities.add(new SimpleGrantedAuthority(privilege.getName())));
		});
		return authorities;
	}

	private Collection<Role> getRoles(Collection<Role> roles){
		return new ArrayList<>(roles);
	}
}

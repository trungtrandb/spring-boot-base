package site.code4fun.mapper;

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import site.code4fun.constant.Status;
import site.code4fun.entity.User;
import site.code4fun.entity.UserPrincipal;

public class UserMapper {
	
	public static UserPrincipal userToUserPrinciple(User user) {
		return UserPrincipal.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.id(user.getId())
				.fullName(user.getFullName())
				.authorities(Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString())))
				.role(user.getRole())
				.isAccountNonLocked(!user.getStatus().equals(Status.LOCK))
				.build();
	}
}

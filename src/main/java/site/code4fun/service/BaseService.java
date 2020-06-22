package site.code4fun.service;

import org.springframework.security.core.context.SecurityContextHolder;

import site.code4fun.entity.UserPrincipal;

public class BaseService {
	protected Long getCurrentId() {
		UserPrincipal currentUser = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return currentUser.getId();
	}
}

package site.code4fun.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import site.code4fun.dto.UserPrincipal;

public class SecurityUtil {
    public synchronized static UserPrincipal getUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (UserPrincipal) principal;
        }
        return null;
    }
}

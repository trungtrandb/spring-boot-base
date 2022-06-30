package site.code4fun.util;

import org.springframework.security.core.context.SecurityContextHolder;
import site.code4fun.model.User;

public class SecurityUtil {
    public synchronized static User getUser(){
        if (SecurityContextHolder.getContext().getAuthentication() != null){
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (principal instanceof User) {
                return (User) principal;
            }
        }
        return null;
    }
}

package site.code4fun.config;

import org.springframework.data.domain.AuditorAware;
import site.code4fun.model.User;
import site.code4fun.util.SecurityUtil;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<User> {

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(SecurityUtil.getUser());
    }
}
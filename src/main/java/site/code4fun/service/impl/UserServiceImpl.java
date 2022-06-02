package site.code4fun.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import site.code4fun.constant.Status;
import site.code4fun.dto.UserPrincipal;
import site.code4fun.exception.DuplicateException;
import site.code4fun.exception.NotFoundException;
import site.code4fun.exception.ValidationException;
import site.code4fun.model.User;
import site.code4fun.repository.UserRepository;
import site.code4fun.service.UserService;
import site.code4fun.util.SecurityUtil;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static site.code4fun.constant.AppConstants.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getPaging(int page, int size, String sortDir, String sort, String query) {
        long id = 0L;
        try{
            id = Long.parseLong(query);
        }catch (Exception ignored){
        }
        page = page > 0 ? page - 1 : page;
        size = size > 0 ? size : DEFAULT_PAGE_SIZE;
        sort = isNotBlank(sort) ? sort : DEFAULT_SORT_COLUMN;
        sortDir = SORT_LIST.contains(sortDir) ? sortDir : DEFAULT_SORT_DIRECTION;
        PageRequest pageReq = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sort);

        return userRepository.findByIdOrUserNameContainsOrFullNameContains(id, query, query, pageReq);
    }

    @Override
    public User getById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    public User getCurrentUser() {
        UserPrincipal userPrincipal = SecurityUtil.getUser();
        if (userPrincipal != null){
            return userRepository.findById(userPrincipal.getId())
                    .orElseThrow(()-> new NotFoundException("Not found user"));
        }
        throw new NotFoundException("User not log in");
    }

    public User create(User user) {
        validateParamAndExistEmail(user);

        if (isNotBlank(user.getPassword()))
            user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setUserName(user.getEmail());
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }

    private void validateParamAndExistEmail(User user) {
        User u = userRepository.findByUserName(user.getEmail());
        if (user.getId() == null){// create
            if (u != null)
                throw new DuplicateException("User with email already existed");

            if (isBlank(user.getPassword()))
                throw new ValidationException("Password must not be null");
        }else if (u != null){ //update
            if (!Objects.equals(u.getId(), user.getId()))
                throw new DuplicateException("User with email already existed");
        }
    }
}

package site.code4fun.service;

import org.springframework.data.domain.Page;
import site.code4fun.model.User;

import java.util.List;

public interface UserService{
    List<User> getAll();

    Page<User> getPaging(int page, int size, String sortDir, String sort, String query);

    User getById(long id);

    User create(User user);

    User getCurrentUser();
}

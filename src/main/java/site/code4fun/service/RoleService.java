package site.code4fun.service;

import org.springframework.data.domain.Page;
import site.code4fun.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> getAll();

    Page<Role> getPaging(int page, int size, String sortDir, String sort, String query);

    void update(Role role);

    Role create(Role role);

    boolean delete(Long id);

    Role getById(Long id);
}

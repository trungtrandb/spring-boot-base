package site.code4fun.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.code4fun.model.Role;
import site.code4fun.repository.RoleRepository;


@Service
@Slf4j
public class RoleService implements BaseService<Role, Long>{

    @Getter
    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository){
        this.repository = repository;
    }
}

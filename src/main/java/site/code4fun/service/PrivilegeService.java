package site.code4fun.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.code4fun.model.Privilege;
import site.code4fun.repository.PrivilegeRepository;

@Service
public class PrivilegeService implements BaseService<Privilege, Long>{

    @Getter
    private final PrivilegeRepository repository;

    @Autowired
    private PrivilegeService(PrivilegeRepository repository){
        this.repository = repository;
    }
}

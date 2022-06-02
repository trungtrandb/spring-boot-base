package site.code4fun.service.impl;

import org.springframework.stereotype.Service;
import site.code4fun.model.Privilege;
import site.code4fun.repository.PrivilegeRepository;
import site.code4fun.service.PrivilegeService;

import java.util.List;

@Service
public class PrivilegeServiceImpl implements PrivilegeService {

    private final PrivilegeRepository repository;

    PrivilegeServiceImpl(PrivilegeRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Privilege> getAll() {
        return repository.findAll();
    }
}

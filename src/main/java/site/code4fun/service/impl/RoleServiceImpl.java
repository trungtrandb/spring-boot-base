package site.code4fun.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.code4fun.exception.NotFoundException;
import site.code4fun.model.Role;
import site.code4fun.repository.RoleRepository;
import site.code4fun.service.RoleService;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static site.code4fun.constant.AppConstants.*;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository){
        this.roleRepository = repository;
    }

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> getPaging(int page, int size, String sortDir, String sort, String query) {
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

        return roleRepository.findByIdOrNameContains(id, query, pageReq);
    }

    @Override
    public void update(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public boolean delete(Long id) {
        Optional<Role> r = roleRepository.findById(id);
        if (r.isPresent()){
            roleRepository.delete(r.get());
            return true;
        }
        throw new NotFoundException("Not found role");
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}

package site.code4fun.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import site.code4fun.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Page<Role> findByIdOrNameContains(Long id, String name, Pageable pageable);
}

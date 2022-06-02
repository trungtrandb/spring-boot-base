package site.code4fun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.code4fun.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}

package site.code4fun.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.code4fun.entity.UserOrganization;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Long>{
	
}

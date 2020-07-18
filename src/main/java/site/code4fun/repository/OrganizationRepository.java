package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import site.code4fun.entity.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long>{
	
	@Query(value = "Select o from Organization o where o.user.id = :userId")
	public List<Organization> findByUserId(@Param("userId")Long userId);

}

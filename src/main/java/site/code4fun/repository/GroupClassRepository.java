package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.GroupClass;

public interface GroupClassRepository extends JpaRepository<GroupClass, Long>{
	
	@Query("Select gc from GroupClass gc where gc.organization.id = :id")
	List<GroupClass> findByOrganizationId(Long id);

	
}

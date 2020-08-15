package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Classes;

public interface ClassRepository extends JpaRepository<Classes, Long>{
	
	@Query("Select new Classes(c.id, c.name || ' - ' || c.groupClass.name, c.note, c.groupClass, c.owner) from Classes c where c.groupClass.id in :ids")
	List<Classes> findByGroupId(List<Long> ids);

	@Query("SELECT c from Classes c where c.owner.id = :id")
	List<Classes> findByOwnerId(Long id);

}

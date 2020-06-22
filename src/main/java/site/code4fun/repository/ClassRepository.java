package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Classes;

public interface ClassRepository extends JpaRepository<Classes, Long>{
	
	@Query("Select c from Classes c where c.groupClass.id in :ids")
	public List<Classes> findByGroupId(List<Long> ids);

}

package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Lession;
public interface LessionRepository extends JpaRepository<Lession, Long>{
	
	@Query(value = "Select s from Lession s where s.classId in :classIds")
	List<Lession> findByClassIds(List<Long> classIds);

	List<Lession> findBySubjectId(Long id);
	
}

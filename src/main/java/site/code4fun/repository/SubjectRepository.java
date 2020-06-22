package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Subject;;
public interface SubjectRepository extends JpaRepository<Subject, Long>{
	
	@Query("Select s from Subject s where s.classId in :classIds")
	public List<Subject> findByClassIds(List<Long> classIds);
	
}

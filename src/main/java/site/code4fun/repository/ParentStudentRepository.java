package site.code4fun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import site.code4fun.entity.ParentStudent;

public interface ParentStudentRepository extends JpaRepository<ParentStudent, Long>{
	
	@Transactional
	public void deleteByStudentId(Long id);

}

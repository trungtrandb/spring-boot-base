package site.code4fun.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import site.code4fun.entity.StudentClass;

public interface StudentClassRepository extends JpaRepository<StudentClass, Long>{
	
	@Transactional
	public void deleteByStudentIdAndClassId(Long studentId, Long classId);
	
	@Transactional
	public void deleteByStudentId(Long studentId);
}

package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.code4fun.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

	List<Student> findByParentId(Long parentId);
	
	List<Student> findByStudentCode(String studentCode);
	
	
}

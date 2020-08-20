package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Student;
import site.code4fun.entity.dto.StudentDTO;

public interface StudentRepository extends JpaRepository<Student, Long>{

	List<Student> findByParentId(Long parentId);
	
	List<Student> findByStudentCode(String studentCode);
	
	
}

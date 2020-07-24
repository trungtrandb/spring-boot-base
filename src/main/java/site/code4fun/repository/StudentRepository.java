package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import site.code4fun.entity.Student;
import site.code4fun.entity.dto.StudentDTO;

public interface StudentRepository extends JpaRepository<Student, Long>{
	
	@Procedure
	public List<StudentDTO> getStudentByClassIds(List<Long> classIds);
	
	public List<Student> findByParentId(Long parentId);
}

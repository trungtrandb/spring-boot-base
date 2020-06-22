package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{
	
	@Query(value = "Select * from tblStudent s join tblStudentClass sc ON s.id = sc.student_id where sc.class_id IN :classIds", nativeQuery = true)
	List<Student> findStudentByClassId(List<Long> classIds);

}

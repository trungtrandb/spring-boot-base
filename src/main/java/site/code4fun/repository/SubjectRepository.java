package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
	
	@Query(value = "Select s from Subject s where s.organizationId = :organizationId")
	public List<Subject> findByOrganizationId(Long organizationId);

}

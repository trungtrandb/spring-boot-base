package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.code4fun.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{
	
	public List<Subject> findByOrganizationId(Long organizationId);

}

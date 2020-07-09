package site.code4fun.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.UserOrganization;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, Long>{
	
	@Transactional
	@Modifying
	@Query(value = "DELETE FROM UserOrganization uo WHERE uo.organizationId = :orgId AND uo.userId = :teacherId")
	public void deleteTeacherOrg(Long teacherId, Long orgId);
}

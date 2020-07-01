package site.code4fun.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.Checkin;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long>{

	@Query(nativeQuery = true, value = "Select c.* FROM tblCheckin c WHERE c.student_id = :studentId AND c.lession_id = :lessionId AND c.class_id =:classId AND DATE_FORMAT(c.created_date, '%Y-%m-%d') = DATE_FORMAT(NOW(), '%Y-%m-%d')")
    public Optional<Checkin> checkExist(Long studentId, Long classId, Long lessionId);
}

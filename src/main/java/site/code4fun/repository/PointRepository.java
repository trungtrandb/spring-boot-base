package site.code4fun.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import site.code4fun.entity.Point;

import javax.transaction.Transactional;
import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long>{

    @Modifying
    @Transactional
    void deleteByStudentId(Long studentId);

    @Modifying
    @Transactional
    @Query(value = "Call deleteOldPoint(:studentId, :subjectId, :sem)", nativeQuery = true)
    void deleteOldPoint(Long studentId, Long subjectId, Byte sem);

    List<Point> findByStudentId(Long studentId);
}

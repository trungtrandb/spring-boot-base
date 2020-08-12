package site.code4fun.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import site.code4fun.entity.Point;

public interface PointRepository extends JpaRepository<Point, Long>{

}

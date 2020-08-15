package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Notify;

public interface NotifyRepository extends JpaRepository<Notify, Long>{
	
	List<Notify> findByStatus(String status);
	
	@Query(value = "CALL updateStatusRead(:notifyIds, :userId);", nativeQuery = true)
	void updateRead(String notifyIds, Long userId);
}

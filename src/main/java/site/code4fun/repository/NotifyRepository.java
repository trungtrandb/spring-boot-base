package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.Notify;

import javax.transaction.Transactional;

public interface NotifyRepository extends JpaRepository<Notify, Long>{
	
	List<Notify> findByStatus(String status);

	@Modifying
	@Transactional
	@Query(value = "CALL updateStatusRead(:notifyIds, :userId);", nativeQuery = true)
	void updateRead(String notifyIds, Long userId);
}

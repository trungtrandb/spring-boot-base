package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.NotifyDevice;

public interface NotifyDeviceRepository extends JpaRepository<NotifyDevice, Long>{
	
	List<NotifyDevice> findByNotifyId(Long id);
	
	@Query(value = "Call findNotifyByStatus(:userId, :status)", nativeQuery = true)
	List<NotifyDevice> getNotifyByStatus(Long userId, boolean status);

}

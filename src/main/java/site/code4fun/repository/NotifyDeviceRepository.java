package site.code4fun.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.NotifyDevice;

public interface NotifyDeviceRepository extends JpaRepository<NotifyDevice, Long>{

	@Query("Select n from NotifyDevice n where n.userId = :id and n.deviceToken = :deviceToken")
	public Optional<NotifyDevice> findByUserIdAndDevice(Long id, String deviceToken);
}

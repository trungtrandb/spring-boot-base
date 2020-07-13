package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import site.code4fun.entity.UserDevice;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long>{

	@Query("Select n from UserDevice n where n.userId = :id and n.deviceToken = :deviceToken")
	public List<UserDevice> findByUserIdAndDeviceToken(Long id, String deviceToken);
}

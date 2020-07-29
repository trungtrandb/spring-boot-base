package site.code4fun.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import site.code4fun.entity.NotifyDevice;

public interface NotifyDeviceRepository extends JpaRepository<NotifyDevice, Long>{
	
	public List<NotifyDevice> findByNotifyId(Long id);

}

package site.code4fun.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.entity.NotifyDevice;
import site.code4fun.repository.NotifyDeviceRepository;

@Service
public class NotifyDeviceService extends BaseService{

	@Autowired
	NotifyDeviceRepository notifyDeviceRepository;
	
	public NotifyDevice insert(String deviceToken) {
		Optional<NotifyDevice> device = notifyDeviceRepository.findByUserIdAndDevice(getCurrentId(), deviceToken);
		if (device.isPresent()) 
			return device.get();
		
		NotifyDevice item = NotifyDevice.builder().userId(getCurrentId()).deviceToken(deviceToken).build();
		
		return notifyDeviceRepository.saveAndFlush(item);
	}
}

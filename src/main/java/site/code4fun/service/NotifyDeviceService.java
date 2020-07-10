package site.code4fun.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.entity.NotifyDevice;
import site.code4fun.repository.NotifyDeviceRepository;

@Service
public class NotifyDeviceService extends BaseService{

	@Autowired
	NotifyDeviceRepository notifyDeviceRepository;
	
	public NotifyDevice insert(String deviceToken) {
		List<NotifyDevice> device = notifyDeviceRepository.findByUserIdAndDevice(getCurrentId(), deviceToken);
		if (device.size() > 0) 
			return device.get(0);
		
		NotifyDevice item = NotifyDevice.builder().userId(getCurrentId()).deviceToken(deviceToken).build();
		
		return notifyDeviceRepository.saveAndFlush(item);
	}
}

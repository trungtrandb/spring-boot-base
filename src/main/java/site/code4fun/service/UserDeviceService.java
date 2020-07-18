package site.code4fun.service;
import java.util.List;

import org.springframework.stereotype.Service;

import site.code4fun.entity.UserDevice;

@Service
public class UserDeviceService extends BaseService{
	
	public UserDevice insert(String deviceToken) {
		List<UserDevice> device = userDeviceRepository.findByUserIdAndDeviceToken(getCurrentId(), deviceToken);
		if (device.size() > 0) return device.get(0);
		
		UserDevice item = UserDevice.builder().userId(getCurrentId()).deviceToken(deviceToken).build();
		
		return userDeviceRepository.saveAndFlush(item);
	}
}

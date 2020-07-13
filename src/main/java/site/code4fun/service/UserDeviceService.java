package site.code4fun.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.code4fun.entity.UserDevice;
import site.code4fun.repository.UserDeviceRepository;

@Service
public class UserDeviceService extends BaseService{

	@Autowired
	UserDeviceRepository UserDeviceRepository;
	
	public UserDevice insert(String deviceToken) {
		List<UserDevice> device = UserDeviceRepository.findByUserIdAndDeviceToken(getCurrentId(), deviceToken);
		if (device.size() > 0) 
			return device.get(0);
		
		UserDevice item = UserDevice.builder().userId(getCurrentId()).deviceToken(deviceToken).build();
		
		return UserDeviceRepository.saveAndFlush(item);
	}
}

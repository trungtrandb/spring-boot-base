package site.code4fun.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import site.code4fun.constant.Queue;
import site.code4fun.constant.Role;
import site.code4fun.constant.Status;
import site.code4fun.entity.User;
import site.code4fun.entity.UserOrganization;
import site.code4fun.repository.UserOrganizationRepository;
import site.code4fun.repository.UserRepository;
import site.code4fun.util.StringUtils;

@Service
public class UserService extends BaseService{

	@Autowired UserRepository userRepository;
	
	@Autowired
	private QueueService queueService;
	
	@Autowired
	private UserOrganizationRepository userOrganizationRepository;
	
	public User create(User u) throws Exception {
		if(u.getOrganizationId() != null) { // Tạo tài khoản cho giáo viên
			if(StringUtils.isNull(u.getEmail())) 
				throw new Exception("Email không được bỏ trống");
			String passWord = StringUtils.randomString();
			u.setUsername(u.getEmail());
			u.setPassword(passWord);
			u.setCreatedBy(getCurrentId());	
			
//			Thêm vào queue gửi mật khẩu cho tài khoản mới đăng ký
			StringBuilder mailContent = new StringBuilder("Tài khoản của bạn đã được đăng ký.");
			mailContent.append("Sử dụng tên tài khoản là địa chỉ email và mật khẩu: ");
			mailContent.append(passWord);
			mailContent.append(" để đăng nhập!");
			
			Map<String, String> mailMess = new HashMap<>();
			mailMess.put("receiver", u.getEmail());
			mailMess.put("subject", "Tạo tài khoản thành công");
			mailMess.put("content", mailContent.toString());
			String mesToQueue = new Gson().toJson(mailMess);
			queueService.sendToQueue(Queue.QUEUE_MAIL, mesToQueue);
		}else {
			if(null == u.getPassword() || null == u.getRePass() || !u.getPassword().equals(u.getRePass())) 
				throw new Exception("Mật khẩu không khớp!");
		}
		
		if(StringUtils.isNull(u.getUsername())) 
			throw new Exception("Tên tài khoản không được bỏ trống");
		User existUser = userRepository.findByUserName(u.getUsername());
		if(null != existUser) 
			throw new Exception("Tài khoản đã tồn tại!");
		
		u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
		u.setStatus(Status.ACTIVE);
		u.setRole(Role.ROLE_USER.getVal());
		u.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		u = userRepository.saveAndFlush(u);
		
		if(null != u.getOrganizationId()) {
			UserOrganization uo = UserOrganization.builder().userId(u.getId()).organizationId(u.getOrganizationId()).build();
			userOrganizationRepository.save(uo);
		}
		
		return u;
	}

	public User getUserInfo() {
		return userRepository.findById(getCurrentId()).get();
	}

	public User updateUser(User u) throws Exception {
		if(null != u.getPassword() && !u.getPassword().equals(u.getRePass())) 
			throw new Exception("Password not match!");
		
		Optional<User> optUser = userRepository.findById(getCurrentId());
		if(!optUser.isPresent()) throw new Exception("User not found!");
		
		User user = optUser.get();
		user.setAddress(u.getAddress());
		user.setAvatar(u.getAvatar());
		user.setEmail(u.getEmail());
		user.setPhone(u.getPhone());
		user.setFullName(u.getFullName());
		user.setIdentityCard(u.getIdentityCard());
		user.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
		if(null != u.getPassword()) {
			user.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
		}
		return userRepository.saveAndFlush(user);
	}
	
	public User getByUserName(String userName) {
		return userRepository.findByUserName(userName);
	}
}

package site.code4fun.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import site.code4fun.constant.Queue;
import site.code4fun.constant.Role;
import site.code4fun.constant.Status;
import site.code4fun.entity.Organization;
import site.code4fun.entity.User;
import site.code4fun.entity.UserOrganization;
import site.code4fun.util.StringUtils;
import site.code4fun.entity.OutputMessage;

@Service
public class UserService extends BaseService{
	
	public User create(User u, String type) throws Exception {
		if("TEACHER".equalsIgnoreCase(type)) {
			Organization org = getCurrentOrganization();
			if(org == null) throw new Exception("Tạo trường trước khi thêm giáo viên!");
			
			u.setOrganizationId(org.getId());
			if(StringUtils.isNull(u.getEmail())) throw new Exception("Email không được bỏ trống");
			User existUser = userRepository.findByUserName(u.getEmail());
			if(existUser != null) {
				u = existUser;
			}else {
				String passWord = StringUtils.randomString();
				u.setUsername(u.getEmail());
				u.setPassword(passWord);
				u.setCreatedBy(getCurrentId());					
				u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
				u.setStatus(Status.ACTIVE);
				u.setRole(Role.ROLE_USER.getVal());
				u.setCreatedDate(new Timestamp(System.currentTimeMillis()));
				u = userRepository.saveAndFlush(u);	
				
//				Thêm vào queue gửi mật khẩu cho tài khoản mới đăng ký
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
			}
			UserOrganization uo = UserOrganization.builder().organizationId(org.getId()).userId(u.getId()).build();
			userOrganizationRepository.save(uo);
		}else {
			if(null == u.getPassword() || null == u.getRePass() || !u.getPassword().equals(u.getRePass())) 
				throw new Exception("Mật khẩu không khớp!");
			
			
			if(StringUtils.isNull(u.getUsername())) 
				throw new Exception("Tên tài khoản không được bỏ trống");
			User existUser = userRepository.findByUserName(u.getUsername());
			if(null != existUser && null == u.getOrganizationId()) 
				throw new Exception("Tài khoản đã tồn tại!");
			
			u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
			u.setStatus(Status.ACTIVE);
			u.setRole(Role.ROLE_USER.getVal());
			u.setCreatedDate(new Timestamp(System.currentTimeMillis()));
			u = userRepository.saveAndFlush(u);
		}
		return u;
	}

	public User getUserInfo() {
		return userRepository.findById(getCurrentId()).get();
	}

	public User updateUser(User u) throws Exception {
		if(null != u.getPassword() && !u.getPassword().equals(u.getRePass())) 
			throw new Exception("Mật khẩu nhập lại không đúng!");
		
		Optional<User> optUser = userRepository.findById(getCurrentId());
		
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

	public boolean resetPassByUserName(String userName) throws Exception {
		User u = userRepository.findByUserName(userName);
		if(null == u) throw new Exception("Tên đăng nhập không tồn tại!");
		String rawPass = StringUtils.randomString();
		u.setPassword(new BCryptPasswordEncoder().encode(rawPass));
		userRepository.save(u);
		mailUtil.sendmail(u.getEmail(), "Thay đổi mật khẩu", "Mật khẩu đã được thay đổi: " + rawPass);
		return true;
	}

	public List<OutputMessage> getListConversion() {
		String userName = getCurrentUser().getUsername();
		return messageRepository.getListConversion(userName);
	}

	public List<OutputMessage> getMessageWithUser(String userName) {
		
		return null;
	}
}

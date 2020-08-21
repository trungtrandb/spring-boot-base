package site.code4fun.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

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
		if(!StringUtils.isNull(u.getPhone()) && !Pattern.matches("^(09|012|08|016|03|07|08|05)\\d{8,}", u.getPhone())) throw new Exception("Số điện thoại không đúng định dạng!");
		if("TEACHER".equalsIgnoreCase(type)) {
			Organization org = getCurrentOrganization();
			if(org == null) throw new Exception("Tạo trường trước khi thêm giáo viên!");
			
			u.setOrganizationId(org.getId());
			
			if(StringUtils.isNull(u.getEmail())) throw new Exception("Email không được bỏ trống");
			Pattern pattern = Pattern.compile("^[a-zA-Z0-9][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$");
			if(!pattern.matcher(u.getEmail()).matches()) throw new Exception("Email không đúng định dạng!");
			
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

				Map<String, String> mailMess = new HashMap<>();
				mailMess.put("receiver", u.getEmail());
				mailMess.put("subject", "Tạo tài khoản thành công");
				String mailContent = "Tài khoản của bạn đã được đăng ký. Sử dụng tên tài khoản là địa chỉ email và mật khẩu: " +
						passWord +
						" để đăng nhập!";
				mailMess.put("content", mailContent);
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
		
		Pattern pattern = Pattern.compile("^[a-zA-Z0-9][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$");
		if((!StringUtils.isNull(u.getEmail()) && !pattern.matcher(u.getEmail()).matches())) throw new Exception("Email không đúng định dạng!");
		if(!StringUtils.isNull(u.getPhone()) && !Pattern.matches("^(09|012|08|016|03|07|08|05)\\d{8,}", u.getPhone())) throw new Exception("Số điện thoại không đúng định dạng!");
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

	public Collection<OutputMessage> getListConversion(String targetUserName) {
		String userName = getCurrentUser().getUsername();
		List<OutputMessage> lst = jMessageRepository.getListConversion(userName);
		List<OutputMessage> lstRest = new ArrayList<>();
		Map<String, OutputMessage> mapRes = new HashMap<>();
		lst.forEach(_item -> {
			if(!userName.equals(_item.getFrom()) || !userName.equals(_item.getTo())) lstRest.add(_item);
		});

		if (!StringUtils.isNull(targetUserName)){
			User targetUser = userRepository.findByUserName(targetUserName);
			for (OutputMessage _item : lstRest ) {
				if (_item.getFrom().equals(targetUserName)){
					mapRes.put(_item.getFrom(), _item);
				}else if (_item.getTo().equals(targetUserName)){
					mapRes.put(_item.getTo(), _item);
				}
			}
			if (mapRes.isEmpty() && null != targetUser){
				OutputMessage outputMessage = OutputMessage.builder()
						.from(targetUser.getUsername())
						.avatar(targetUser.getAvatar())
						.fullName(targetUser.getFullName())
						.build();
				mapRes.put(targetUserName, outputMessage);
			}
		}

		lstRest.forEach(_item ->{
			if(userName.equals(_item.getFrom())) {
				_item.setFrom(_item.getTo());
				mapRes.put(_item.getTo(), _item);
			}else {
				mapRes.put(_item.getFrom(), _item);
			}
		});
		return mapRes.values();
	}

	public List<OutputMessage> getMessage(String userName , Long page, Integer size) {
		int limit = null != size ? size : 10;
		Long offset = null != page ? (page - 1) * limit : 0;
		return jMessageRepository.getMessage(getCurrentUser().getUsername(), userName, limit, offset);
	}
}

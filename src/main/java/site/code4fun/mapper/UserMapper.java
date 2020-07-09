package site.code4fun.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import site.code4fun.constant.Status;
import site.code4fun.entity.User;
import site.code4fun.entity.UserPrincipal;

public class UserMapper implements RowMapper<User>{
	
	public static UserPrincipal userToUserPrinciple(User user) {
		return UserPrincipal.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.id(user.getId())
				.fullName(user.getFullName())
				.avatar(user.getAvatar())
				.authorities(Arrays.asList(new SimpleGrantedAuthority(user.getRole().toString())))
				.role(user.getRole())
				.isAccountNonLocked(!user.getStatus().equals(Status.LOCK.getVal()))
				.build();
	}

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		return User.builder()
				.id(rs.getLong("id"))
				.status(rs.getString("status"))
				.address(rs.getString("address"))
				.avatar(rs.getString("avatar"))
				.createdBy(rs.getLong("created_by"))
				.createdDate(rs.getTimestamp("created_date"))
				.email(rs.getString("email"))
				.fullName(rs.getString("full_name"))
				.phone(rs.getString("phone"))
				.username(rs.getString("user_name"))
				.OrganizationId(rs.getLong("organization_id"))
				.build();
	}
}

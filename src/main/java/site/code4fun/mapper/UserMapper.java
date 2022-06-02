package site.code4fun.mapper;

import com.googlecode.jmapper.JMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import site.code4fun.constant.Status;
import site.code4fun.model.User;
import site.code4fun.dto.UserDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserMapper implements RowMapper<User>{

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		return User.builder()
				.id(rs.getLong("id"))
				.status(Status.valueOf(rs.getString("status")))
				.address(rs.getString("address"))
				.avatar(rs.getString("avatar"))
				.createdBy(rs.getLong("created_by"))
				.created(rs.getTimestamp("created").toInstant())
				.email(rs.getString("email"))
				.fullName(rs.getString("full_name"))
				.phone(rs.getString("phone"))
				.userName(rs.getString("user_name"))
				.build();
	}

	public List<UserDTO> usersToUserDTOs(List<User> users) {
		return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(Collectors.toList());
	}

	public UserDTO userToUserDTO(User user) {
		JMapper<UserDTO, User> mapper = new JMapper<>(UserDTO.class, User.class);
		return mapper.getDestination(user);
	}

	public List<User> userDTOsToUsers(List<UserDTO> userDTOs) {
		return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
	}

	public User userDTOToUser(UserDTO userDTO) {
		if (userDTO == null) {
			return null;
		} else {
			JMapper<User, UserDTO> mapper = new JMapper<>(User.class, UserDTO.class);
			return mapper.getDestination(userDTO);
		}
	}
}

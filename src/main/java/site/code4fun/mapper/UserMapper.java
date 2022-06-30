package site.code4fun.mapper;

import com.googlecode.jmapper.JMapper;
import org.springframework.stereotype.Service;
import site.code4fun.dto.UserDTO;
import site.code4fun.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserMapper{

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

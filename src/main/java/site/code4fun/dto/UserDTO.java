package site.code4fun.dto;

import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;
import lombok.Data;
import site.code4fun.model.Role;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserDTO{

	@JMap
	private Long id;

	@JMap
	private String username;

	@JMap
	private String fullName;

	@JMap
	private String email;

	@JMap
	private String status;

	@JMap
	private String avatar;

	@JMap
	private String roles;
	private List<Long> roleIds;

	@JMap
	private String address;

	@JMap
	private Integer gender;

	@JMapConversion(from={"roles"}, to={"roles"})
	public String conversion(Set<Role> roles){
		this.roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
		return roles.stream().map(Role::getName).collect(Collectors.joining(", "));
	}
}

package site.code4fun.entity;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/* Author: TrungTQ
 * Chỉ dùng để phần quyền authen, không dùng cho việc khác
 */

@Builder
public class UserPrincipal implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String username;
	
	@Getter
	@Setter
	private String fullName;
	
	@Getter
	@Setter
	private String avatar;
	
	private Long id;
	
	@JsonIgnore
	private String password;
	
	@Builder.Default
	private Collection<? extends GrantedAuthority> authorities = Collections.emptyList();
	
	@Builder.Default
	private boolean isAccountNonExpired = true;
	
	@Builder.Default
	private boolean isEnabled = true;
	
	@Builder.Default
	private boolean isAccountNonLocked = true ;
	
	@Builder.Default
	private boolean isCredentialsNonExpired = true;
	private String role;
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	public void setAccountNonExpired(boolean isAccountNonExpired) {
		this.isAccountNonExpired = isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	public void setAccountNonLocked(boolean isAccountNonLocked) {
		this.isAccountNonLocked = isAccountNonLocked;
	}

	public void setCredentialsNonExpired(boolean isCredentialsNonExpired) {
		this.isCredentialsNonExpired = isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}


	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
}
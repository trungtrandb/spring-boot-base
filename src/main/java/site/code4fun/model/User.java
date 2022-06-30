package site.code4fun.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import site.code4fun.constant.AppConstants;
import site.code4fun.constant.Status;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = AppConstants.TABLE_PREFIX + "User")
public class User implements UserDetails {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    
	@JsonProperty(access = Access.WRITE_ONLY)
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = AppConstants.TABLE_PREFIX + "Users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Email
    private String email;

    @Column(columnDefinition = "int default 0")
    private Integer gender;
    private String address;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreatedDate
    @Column(updatable = false)
    private Instant created = Instant.now();

    @CreatedBy
    @Column(name = "created_by")
    private Long createdBy;

    @LastModifiedDate
    private Instant updated;

    @LastModifiedBy
    @Column(name = "updated_by")
    private Long updatedBy;
    
    @Column(name = "identity_card")
    private String identityCard;
    
    @JsonIgnore
    @Column(name = "oauth2_provider")
    private String oauth2Provider;
    
    @JsonIgnore
    @Column(name = "oauth2_id")
    private String oauth2Id;
    
    private String avatar;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPrivileges().forEach(privilege -> authorities.add(new SimpleGrantedAuthority(privilege.getName())));
        });
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
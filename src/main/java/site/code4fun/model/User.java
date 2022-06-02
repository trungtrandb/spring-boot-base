package site.code4fun.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import site.code4fun.constant.Status;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;
    
	@JsonProperty(access = Access.WRITE_ONLY)
    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;
    
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

    @Column(name = "created_by")
    private Long createdBy;

    private Timestamp updated;

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
}
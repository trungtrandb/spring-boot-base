package site.code4fun.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import site.code4fun.constant.Status;


@Entity 
@Table(name = "tblUser")
@Data
@AllArgsConstructor
@Builder
public class User{
	
	public User() {}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@NotBlank(message = "Username must not be blank")
    @Column(name = "user_name", nullable = false, unique = true)
    private String username;
    
	@JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "pass_word", nullable = false)
    private String password;
    
    @Transient
    private String rePass;
    
//    @Enumerated(value = EnumType.STRING)
    private String role;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Email
    private String email;
    private int gender;    
    private String address;    
    private String phone;
    
    @Enumerated(value = EnumType.STRING)
    private Status status;
    
    @Column(name = "created_date")
    private Timestamp createdDate;
    
    @Column(name = "created_by")
    private Integer createdBy;
    
    @Column(name = "updated_date")
    private Timestamp updatedDate;
    
    @Column(name = "updated_by")
    private Integer updatedBy;
    
    @Column(name = "identity_card")
    private Integer identityCard;
    
    @JsonIgnore
    @Column(name = "oauth2_provider")
    private String oauth2Provider;
    
    @JsonIgnore
    @Column(name = "oauth2_id")
    private String oauth2Id;
    
    @Column(name = "avatar")
    private String avatar;
}
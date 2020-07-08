package site.code4fun.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity 
@Table(name = "tblUser")
@Data
@AllArgsConstructor
@Builder
public class User{
	
	public User() {}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

	@NotBlank(message = "Tên đăng nhập không được bỏ trống")
    @Column(name = "user_name", nullable = false, unique = true)
	protected String username;
    
	@JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "pass_word", nullable = false)
	protected String password;
    
	protected String role;
    
    @Column(name = "full_name")
    protected String fullName;
    
    @Email
    @NotBlank(message = "Email không đúng định dạng")
    protected String email;
    protected int gender;    
    protected String address;    
    protected String phone;
    
    protected String status;
    
    @Column(name = "created_date")
    protected Timestamp createdDate;
    
    @Column(name = "created_by")
    protected Long createdBy;
    
    @Column(name = "updated_date")
    protected Timestamp updatedDate;
    
    @Column(name = "updated_by")
    protected Long updatedBy;
    
    @Column(name = "identity_card")
    protected String identityCard;
    
    @JsonIgnore
    @Column(name = "oauth2_provider")
    protected String oauth2Provider;
    
    @JsonIgnore
    @Column(name = "oauth2_id")
    protected String oauth2Id;
    
    @Column(name = "avatar")
    protected String avatar;
    
    
    // DTO
    @Transient
    protected String rePass;
    
    @Transient
    protected Long OrganizationId;
    
    @Transient
    protected String organizationName;
}
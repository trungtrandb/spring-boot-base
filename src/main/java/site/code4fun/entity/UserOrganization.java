package site.code4fun.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "tblUserOrganization")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserOrganizationKey.class)
public class UserOrganization{

	@Id
	@Column(name = "user_id")
	private Long userId;
	
	@Id
	@Column(name = "organization_id")
	private Long organizationId;
}

@Data
class UserOrganizationKey implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private Long organizationId;
}

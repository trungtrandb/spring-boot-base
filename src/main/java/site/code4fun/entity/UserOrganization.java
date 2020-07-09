package site.code4fun.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
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
	private Long userId;
	
	@Id
	private Long organizationId;
}

@Embeddable
class UserOrganizationKey implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "organization_id")
	private Long organizationId;
	
}

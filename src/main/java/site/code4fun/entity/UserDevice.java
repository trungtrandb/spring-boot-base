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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tblUserDevice")
@IdClass(UserDeviceKey.class)
public class UserDevice {
	
	@Id
	private String deviceToken;
	
	@Id
	private Long userId;
}

@Embeddable
@Data
class UserDeviceKey implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "device_token")
	private String deviceToken;
}

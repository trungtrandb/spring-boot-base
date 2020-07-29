package site.code4fun.entity;

import java.io.Serializable;
import java.sql.Timestamp;

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
@Table(name = "tblNotifyDevice")
@IdClass(NotifyDeviceKey.class)
public class NotifyDevice {
	
	@Id
	protected Long notifyId;
	
	@Id
	protected String deviceToken;
	protected String status; // COMPLETE/ERROR/PENDING
	protected String note;
	
	@Column(name = "user_id")
	protected Long userId;
	
	@Column(name = "created_by")
	protected Long createdBy;
	
	@Column(name = "created_date")
	protected Timestamp createdDate;
	
	@Column(name = "updated_date")
	protected Timestamp updatedDate;
}

@Embeddable
@Data
class NotifyDeviceKey implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "notify_id")
	protected Long notifyId;
	
	@Column(name = "device_token")
	protected String deviceToken;
}

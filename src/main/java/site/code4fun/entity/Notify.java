package site.code4fun.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.code4fun.constant.Status;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tblNotify")
public class Notify {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String content;
	
	@Column(name = "device_token")
	private String deviceToken;
	private Status status;
	private String note;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	@Column(name = "created_by")
	private Long createdBy;
}

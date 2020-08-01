package site.code4fun.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "tblMessage")
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	
	@Column(name = "send_to")
	protected String to;
	
	@Column(name = "send_from")
	protected String from;
	
	protected String text;
	
	@Column(name = "created_date")
	protected Timestamp createdDate;
	
	protected String status;
	
	
}

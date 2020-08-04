package site.code4fun.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Entity
@Table(name = "tblMessage")
@AllArgsConstructor
@NoArgsConstructor
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name = "send_to")
	protected String to;
	
	@Column(name = "send_to_id")
	protected Long toId;
	
	@Column(name = "send_from_id")
	protected Long fromId;
	
	@Column(name = "send_from")
	protected String from;
	
	protected String text;
	
	@Column(name = "created_date")
	protected Timestamp createdDate;
	
	protected String status;	
}

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

@Entity
@Table(name = "tblCheckin")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checkin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "student_id")
	private Long studentId;
	
	@Column(name = "lession_id")
	private Long lessionId;
	
	@Column(name = "class_id")
	private Long classId;
	
	@Column(name = "created_by")
	private Long createdBy;
	
	@Column(name = "created_date")
	private Timestamp createdDate;
	
	private boolean present;
}

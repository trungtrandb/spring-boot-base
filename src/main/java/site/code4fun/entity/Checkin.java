package site.code4fun.entity;

import java.sql.Timestamp;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "tblCheckin")
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Checkin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name = "student_id")
	protected Long studentId;
	
	@Column(name = "lession_id")
	protected Long lessionId;
	
	@Transient
	protected Long classId;
	
	@Column(name = "created_by")
	protected Long createdBy;

	protected String note;
	
	@Column(name = "created_date")
	protected Timestamp createdDate;

	protected Boolean present;
}

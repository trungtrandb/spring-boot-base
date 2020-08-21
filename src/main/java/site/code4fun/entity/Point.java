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

@Entity
@Table(name = "tblPoint")
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class Point {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name = "student_id")
	protected Long studentId;
	
	@Column(name = "subject_id")
	protected Long subjectId;
	
	protected Float point;
	
	protected Byte multiple;
	
	protected Byte sem;
	
	@Column(name = "created_by")
	protected Long createdBy;
	
	@Column(name = "created_date")
	protected Timestamp createdDate;
}

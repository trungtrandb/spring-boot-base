package site.code4fun.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@Entity
@Table(name = "tblLession")
@NoArgsConstructor
@AllArgsConstructor
public class Lession {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	private String description;
	
	@Column(name = "subject_id")
	private Long subjectId;
	
	@Column(name = "class_id")
	private Long classId;
	
	@Column(name = "start_time")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private Timestamp startTime;
	
	@Column(name = "end_time")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private Timestamp endTime;
	
	@Column(name = "user_id")
	private Long userId; // Người dạy
	
	// DTO
	@Transient
	private String className;
}

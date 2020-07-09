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
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tblStudentClass")
@IdClass(StudentClassKey.class)
public class StudentClass {

	@Id
	private Long studentId;
	
	@Id
	private Long classId;
}

@Embeddable
@Data
class StudentClassKey implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "class_id")
	private Long classId;
	
	@Column(name = "student_id")
	private Long studentId;
}

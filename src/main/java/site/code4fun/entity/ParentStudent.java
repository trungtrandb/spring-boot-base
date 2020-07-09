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
@Table(name = "tblParentStudent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ParentStudentKey.class)
public class ParentStudent {
	
	@Id
	private Long studentId;
	
	@Id
	private Long userId;
}

@Embeddable
@Data
class ParentStudentKey implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "student_id")
	private Long studentId;
}

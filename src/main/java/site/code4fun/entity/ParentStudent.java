package site.code4fun.entity;

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
@Table(name = "tblParentStudent")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentStudent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "student_id")
	private Long studentId;
	
	@Column(name = "user_id")
	private Long userId;
}

package site.code4fun.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tblClass")
public class Classes {
	
	public Classes(Long id, String name, String note, GroupClass groupClass, User owner, String status, Integer schoolYear) {
		this.id = id;
		this.name = name;
		this.note = note;
		this.groupClass = groupClass;
		this.owner = owner;
		this.status = status;
		this.schoolYear = schoolYear;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	protected String name;
	protected String note;
	protected String status;

	@ManyToOne
	@JoinColumn(name="owner_id")
	protected User owner; //Chủ nhiệm lớp

	@Column(name = "school_year")
	protected Integer schoolYear;

	@ManyToOne
	@JoinColumn(name="group_class_id")
	protected GroupClass groupClass;
}

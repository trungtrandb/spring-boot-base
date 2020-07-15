package site.code4fun.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	
	public Classes(Long id, String name, String note, GroupClass groupClass, User owner) {
		this.id = id;
		this.name = name;
		this.note = note;
		this.groupClass = groupClass;
		this.owner = owner;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	protected String name;
	protected String note;

	@ManyToOne
	@JoinColumn(name="owner_id")
	protected User owner; //Chủ nhiệm lớp
	
	@ManyToOne
	@JoinColumn(name="group_class_id")
	protected GroupClass groupClass;
}

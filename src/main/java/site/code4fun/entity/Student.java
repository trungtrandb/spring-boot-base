	package site.code4fun.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "tblStudent")
@NoArgsConstructor
@AllArgsConstructor
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address;
	private String phone;
	private String email;
	private String note;
	
	@Column(name = "date_of_birth")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	private Timestamp dateOfBirth;
	
//	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
//    @JoinTable(
//        name = "tblStudentClass", 
//        joinColumns = { @JoinColumn(name = "student_id") }, 
//        inverseJoinColumns = { @JoinColumn(name = "class_id") }
//    )	
//	@JsonIgnore
//	private List<Classes> classes;
}

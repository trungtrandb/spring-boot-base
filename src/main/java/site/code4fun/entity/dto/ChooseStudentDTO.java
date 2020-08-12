package site.code4fun.entity.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import site.code4fun.entity.Student;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
public class ChooseStudentDTO extends Student{
	private Long id;
	private String avatar;
	private String name;
	private Timestamp dateOfBirth;
	private String className;
	private String groupClassName;
	private String schoolName;
}

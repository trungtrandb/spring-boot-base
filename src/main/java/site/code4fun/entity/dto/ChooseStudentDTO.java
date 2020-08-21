package site.code4fun.entity.dto;

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
	private String className;
	private String groupClassName;
	private String schoolName;
	private Long schoolId;
}

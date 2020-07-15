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
public class StudentDTO extends Student{
	private String parentPhoneOrEmail;
	private String parentName;
	private String className;
}

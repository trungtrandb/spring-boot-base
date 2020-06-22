package site.code4fun.entity.dto;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDTO {
	private Long id;
	private String name;
	private Timestamp dateOfBirth;
	private String address;
	private String phone;
	private String email;
	private String note;
	private Long[] classes;	
	private Long[] subjects;
	private String parentPhoneOrEmail;
	private String parentName;
	private String className;
}

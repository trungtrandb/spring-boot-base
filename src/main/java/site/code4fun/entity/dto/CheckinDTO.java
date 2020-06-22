package site.code4fun.entity.dto;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckinDTO {
	private Long classId;
	private String className;
	
	
	private Long subjectId;
	private String subjectName;
	private Timestamp createdDate;
	private String createdName;
	private int totalCheckin;
	private int totalPresent;
	private int totalAbsent;
}

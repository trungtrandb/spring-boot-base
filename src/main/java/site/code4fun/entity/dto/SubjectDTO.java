package site.code4fun.entity.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.code4fun.entity.Subject;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
	private Long id;
	private String name;
	private String note;
	private Long organizationId;
	private String organizationName;
	private String status;
	private Long createdBy;
	private Timestamp createdDate;
	private Long updated_by;
	private Timestamp updatedTime;
	
	public static SubjectDTO fromEntity(Subject s) {
		return SubjectDTO.builder()
		.id(s.getId())
		.name(s.getName())
		.createdBy(s.getCreatedBy())
		.createdDate(s.getCreatedDate())
		.note(s.getNote())
		.status(s.getStatus())
		.organizationId(s.getOrganizationId())
		.updated_by(s.getUpdated_by())
		.updatedTime(s.getUpdatedTime())
		.build();
	}
}

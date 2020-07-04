package site.code4fun.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassDTO {

	private Long id;
	private String name;
	private String note;
	private boolean isOrganizationClass;
	private Long groupClassId; 
	private Long ownerId;
	private String owenerName;
}

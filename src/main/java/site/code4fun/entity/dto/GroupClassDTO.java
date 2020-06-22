package site.code4fun.entity.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupClassDTO {
	private Long id;
	private String name;
	private Long organizationId;
}

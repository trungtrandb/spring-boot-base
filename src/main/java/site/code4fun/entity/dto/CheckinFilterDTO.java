package site.code4fun.entity.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckinFilterDTO {
	private Long classId;
	private Long lessionId;
	private Long organizationId;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
	private Timestamp createdDate;
}

package site.code4fun.dto;

import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import site.code4fun.constant.Status;

import java.util.Date;

@Data
public class CategoryDTO {

	@JMap
	private Long id;

	@JMap
	private String name;

	@JMap
	private Status status;

	@JMap
	private Date created;

	@JMap
	private Date updated;
}

package site.code4fun.dto;

import lombok.Data;
import site.code4fun.constant.Status;

import java.util.Date;

@Data
public class PostDTO {

	private Long id;
	private String name;
	private String slug;
	private Status status;
	private String content;
	private Long categoryId;
	private String categoryName;
	private Date created;
	private Date updated;

}

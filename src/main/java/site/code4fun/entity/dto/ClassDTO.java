package site.code4fun.entity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import site.code4fun.entity.Classes;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClassDTO extends Classes{

	protected Long groupClassId; 
	protected Long ownerId;
	protected String owenerName;
}

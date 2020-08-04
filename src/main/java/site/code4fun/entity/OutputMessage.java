package site.code4fun.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class OutputMessage extends Message{
	
	private String fullName;
	private String avatar;
}

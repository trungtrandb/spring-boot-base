package site.code4fun.entity;

import javax.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutputMessage extends Message{
	
	@Column(name = "full_name")
	private String fullName;
	private String avatar;
}

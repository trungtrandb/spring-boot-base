package site.code4fun;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Response {
	private Integer code;
	private String message;
	private Object data;
}

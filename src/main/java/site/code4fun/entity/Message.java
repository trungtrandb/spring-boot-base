package site.code4fun.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
	private String to;
    private String from;
    private String text;
}

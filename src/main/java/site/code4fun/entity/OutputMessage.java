package site.code4fun.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OutputMessage {

	private String fullName;
	private String avatar;
    private String from;
    private String text;
    private String time;
}

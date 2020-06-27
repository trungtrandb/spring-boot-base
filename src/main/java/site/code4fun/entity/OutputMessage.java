package site.code4fun.entity;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OutputMessage {

	private String fullName;
	private String avatar;
    private Long from;
    private String text;
    private Timestamp time;
    private Long classId;
}

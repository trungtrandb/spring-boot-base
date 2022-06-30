package site.code4fun.dto.request;

import lombok.Data;
import site.code4fun.constant.Status;

@Data
public class PostRequest {
    private Long id;
    private String name;
    private String slug;
    private String content;
    private Status status;
    private Long categoryId;
}

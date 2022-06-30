package site.code4fun.dto.request;

import lombok.Data;
import site.code4fun.constant.Status;

@Data
public class CategoryRequest {
    private Long id;
    private String name;
    private Status status;
}

package site.code4fun.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Long id;
    private String name;
    private List<Long> privilegeIds;
}

package site.code4fun.dto.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserRequest {
    private Long id;
    private String userName;

    @NotBlank
    private String fullName;

    @NotBlank
    @Email
    private String email;
    private String status;
    private String avatar;
    private String password;
    private String address;
    private List<Long> roles;
    private Integer gender;
}

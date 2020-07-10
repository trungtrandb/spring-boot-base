package site.code4fun.entity.dto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import site.code4fun.entity.User;

@Getter
@Setter
@SuperBuilder
public class UserDTO extends User {
    protected String organizationName;
	protected String studentName;
	protected String deviceToken;
}

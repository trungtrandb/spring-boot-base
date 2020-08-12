package site.code4fun.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import site.code4fun.entity.dto.StudentDTO;

public class StudentDTOMapper implements RowMapper<StudentDTO>{

	@Override
	public StudentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		return StudentDTO.builder()
				.address(rs.getString("address"))
				.classId(rs.getLong("class_id"))
				.parentName(rs.getString("full_name"))
				.parentId(rs.getLong("user_id"))
				.parentPhoneOrEmail(rs.getString("user_email"))
				.phone(rs.getString("phone"))
				.email(rs.getString("email"))
				.dateOfBirth(rs.getTimestamp("date_of_birth"))
				.id(rs.getLong("id"))
				.studentCode(rs.getString("student_code"))
				.name(rs.getString("name"))
				.avatar(rs.getString("avatar"))
				.build();
	}

}

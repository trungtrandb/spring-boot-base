package site.code4fun.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.Classes;
import site.code4fun.entity.NotifyDevice;
import site.code4fun.entity.User;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.entity.dto.UserDTO;

@Repository
public class JStudentRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<StudentDTO> findStudentByClassId(List<Long> classIds) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("classIds", classIds);
		StringBuilder sql = new StringBuilder(
				"SELECT s.*, c.name as class_name, u.full_name, sc.class_id FROM tblStudent s ");
		sql.append("JOIN tblParentStudent ps ON s.id = ps.student_id ");
		sql.append("JOIN tblStudentClass sc ON s.id = sc.student_id ");
		sql.append("JOIN tblClass c on sc.class_id = c.id ");
		sql.append("JOIN tblUser u on u.id = ps.user_id ");
		sql.append("WHERE sc.class_id IN (:classIds) ");
		List<StudentDTO> lstRes = new ArrayList<>();
		if (classIds.size() == 0) return lstRes;
		jdbcTemplate.query(sql.toString(), parameters, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Long[] lstClass = { (Long) rs.getLong("class_id") };
				StudentDTO st = StudentDTO.builder()
						.id(rs.getLong("id"))
						.address(rs.getString("address"))
						.className(rs.getString("class_name"))
						.dateOfBirth(rs.getTimestamp("date_of_birth"))
						.name(rs.getString("name"))
						.phone(rs.getString("phone"))
						.note(rs.getString("note"))
						.parentName(rs.getString("full_name"))
						.classes(lstClass)
						.build();
				lstRes.add(st);
			}
		});
		return lstRes;
	}

	public List<User> findParentByStudentId(Long studentId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentId", studentId);
		StringBuilder sql = new StringBuilder("SELECT u.* FROM tblStudent s ");
		sql.append("JOIN tblParentStudent ps ON s.id = ps.student_id ");
		sql.append("JOIN tblUser u ON u.id = ps.user_id ");
		sql.append("WHERE s.id = :studentId");
		return jdbcTemplate.query(sql.toString(), parameters, (rs, rowNum) -> User.builder()
				.username(rs.getString("user_name")).fullName(rs.getString("full_name")).build());
	}

	public List<Classes> findClassByStudentId(Long studentId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentId", studentId);
		StringBuilder sql = new StringBuilder("SELECT c.* FROM tblStudent s ");
		sql.append("JOIN tblStudentClass sc ON s.id = sc.student_id ");
		sql.append("JOIN tblClass c ON c.id = sc.class_id ");
		sql.append("WHERE s.id = :studentId");
		return jdbcTemplate.query(sql.toString(), parameters,
				(rs, rowNum) -> Classes.builder().name(rs.getString("name")).id(rs.getLong("id")).build());
	}
	
	public List<UserDTO> findParentByClassIds(List<Long> classIds) {
		if(classIds.size() == 0 ) return new ArrayList<>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("classIds", classIds);
		StringBuilder sql = new StringBuilder("SELECT u.*, ud.device_token, s.name as student_name FROM tblUser u ");
		sql.append("LEFT JOIN tblUserDevice ud on ud.user_id = u.id ");
		sql.append("JOIN tblStudent s ON s.parent_id = u.id ");
		sql.append("WHERE sc.class_id IN (:classIds)");
		return jdbcTemplate.query(sql.toString(), parameters,
				(rs, rowNum) -> UserDTO.builder()
				.id(rs.getLong("id"))
				.fullName(rs.getString("full_name"))
				.avatar(rs.getString("avatar"))
				.phone(rs.getString("phone"))
				.email(rs.getString("email"))
				.address(rs.getString("address"))
				.createdBy(rs.getLong("created_by"))
				.createdDate(rs.getTimestamp("created_date"))
				.updatedDate(rs.getTimestamp("updated_date"))
				.updatedBy(rs.getLong("updated_by"))
				.status(rs.getString("status"))
				.username(rs.getString("user_name"))
				.studentName(rs.getString("student_name"))
				.deviceToken(rs.getString("device_token"))
				.build());
	}

	public List<NotifyDevice> findParentDeviceByStudentId(Long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentId", id);
		StringBuilder sql = new StringBuilder("SELECT nd.* FROM tblNotifyDevice nd ");
		sql.append("JOIN tblParentStudent ps ON ps.user_id = nd.user_id ");
		sql.append("WHERE ps.student_id = :studentId");
		return jdbcTemplate.query(sql.toString(), parameters, (rs, rowNum) -> NotifyDevice.builder()
				.id(rs.getLong("id")).deviceToken(rs.getString("device_token")).userId(rs.getLong("user_id")).build());
	}
}

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

import site.code4fun.entity.UserDevice;
import site.code4fun.entity.dto.StudentDTO;
import site.code4fun.entity.dto.UserDTO;
import site.code4fun.mapper.StudentDTOMapper;

@Repository
public class JStudentRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<StudentDTO> findStudentByClassId(List<Long> classIds) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("classIds", classIds);
		StringBuilder sql = new StringBuilder(
				"SELECT s.*, c.name as class_name, u.full_name FROM tblStudent s ");
		sql.append("JOIN tblClass c on s.class_id = c.id ");
		sql.append("JOIN tblUser u on u.id = s.parent_id ");
		sql.append("WHERE s.class_id IN (:classIds) ");
		List<StudentDTO> lstRes = new ArrayList<>();
		if (classIds.size() == 0) return lstRes;
		jdbcTemplate.query(sql.toString(), parameters, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				StudentDTO st = StudentDTO.builder()
						.id(rs.getLong("id"))
						.address(rs.getString("address"))
						.className(rs.getString("class_name"))
						.dateOfBirth(rs.getTimestamp("date_of_birth"))
						.name(rs.getString("name"))
						.phone(rs.getString("phone"))
						.note(rs.getString("note"))
						.parentName(rs.getString("full_name"))
						.classId(rs.getLong("class_id"))
						.build();
				lstRes.add(st);
			}
		});
		return lstRes;
	}

	public StudentDTO findById(Long studentId) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentId", studentId);
		StringBuilder sql = new StringBuilder("SELECT s.*, u.email as user_email, u.full_name, u.id as user_id FROM tblStudent s ");
		sql.append("JOIN tblUser u ON u.id = s.parent_id ");
		sql.append("WHERE s.id = :studentId");
		List<StudentDTO> lst = jdbcTemplate.query(sql.toString(), parameters,new StudentDTOMapper());
		return lst.size() > 0 ? lst.get(0) : null;
	}
	
	public List<UserDTO> findParentByClassIds(List<Long> classIds) {
		if(classIds.size() == 0 ) return new ArrayList<>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("classIds", classIds);

		StringBuilder sql = new StringBuilder("SELECT u.*, ud.device_token, s.name as student_name FROM tblUser u ");
		sql.append("LEFT JOIN tblUserDevice ud on ud.user_id = u.id ");
		sql.append("JOIN tblStudent s ON s.parent_id = u.id ");
		sql.append("WHERE s.class_id IN (:classIds)");
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

	public List<UserDevice> findParentDeviceByStudentId(Long id) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentId", id);
		StringBuilder sql = new StringBuilder("SELECT ud.* FROM tblStudent s ");
		sql.append("JOIN tblUserDevice ud ON s.parent_id = ud.user_id ");
		sql.append("WHERE s.id = :studentId");
		return jdbcTemplate.query(sql.toString(), parameters, (rs, rowNum) -> 
			UserDevice.builder()
				.id(rs.getLong("id"))
				.deviceToken(rs.getString("device_token"))
				.userId(rs.getLong("user_id"))
				.build());
	}
}

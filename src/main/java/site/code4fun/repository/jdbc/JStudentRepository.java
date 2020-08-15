package site.code4fun.repository.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.UserDevice;
import site.code4fun.entity.dto.ChooseStudentDTO;
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
                "SELECT s.*, c.name as class_name, u.full_name, u.user_name FROM tblStudent s ");
        sql.append("JOIN tblClass c on s.class_id = c.id ");
        sql.append("JOIN tblUser u on u.id = s.parent_id ");
        sql.append("WHERE s.class_id IN (:classIds) ");
        List<StudentDTO> lstRes = new ArrayList<>();
        if (classIds.size() == 0) return lstRes;
		return jdbcTemplate.query(sql.toString(), parameters, (rs, rowNum) ->
			StudentDTO.builder()
					.id(rs.getLong("id"))
					.studentCode(rs.getString("student_code"))
					.address(rs.getString("address"))
					.className(rs.getString("class_name"))
					.dateOfBirth(rs.getTimestamp("date_of_birth"))
					.name(rs.getString("name"))
					.phone(rs.getString("phone"))
					.note(rs.getString("note"))
					.parentName(rs.getString("full_name"))
					.parentPhoneOrEmail(rs.getString("user_name"))
					.classId(rs.getLong("class_id"))
					.build());
    }

    public StudentDTO findById(Long studentId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("studentId", studentId);
        StringBuilder sql = new StringBuilder("SELECT s.*, u.email as user_email, u.full_name, u.id as user_id FROM tblStudent s ");
        sql.append("JOIN tblUser u ON u.id = s.parent_id ");
        sql.append("WHERE s.id = :studentId");
        List<StudentDTO> lst = jdbcTemplate.query(sql.toString(), parameters, new StudentDTOMapper());
        return lst.size() > 0 ? lst.get(0) : null;
    }

    public List<UserDTO> findParentByClassIds(List<Long> classIds) {
        if (classIds.size() == 0) return new ArrayList<>();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("classIds", classIds);

        StringBuilder sql = new StringBuilder("SELECT u.*, ud.device_token, s.name as student_name FROM tblUser u ");
        sql.append("LEFT JOIN tblUserDevice ud on ud.user_id = u.id ");
        sql.append("JOIN tblStudent s ON s.parent_id = u.id ");
        sql.append("WHERE s.class_id IN (:classIds) ");
        sql.append("GROUP BY u.id");
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
                        .deviceToken(rs.getString("device_token"))
                        .userId(rs.getLong("user_id"))
                        .build());
    }


    public List<ChooseStudentDTO> getListChooseSt(Long parentId) {
        StringBuilder sql = new StringBuilder(
                "SELECT s.id as id,s.name as name,s.avatar as avatar,s.date_of_birth as date_of_birth, c.name as class_name, g.name as group_class_name, o.name as school_name FROM tblStudent s ");
        sql.append("JOIN tblClass c on s.class_id = c.id ");
        sql.append("JOIN tblGroupClass g on c.group_class_id = g.id ");
        sql.append("JOIN tblOrganization o on g.organization_id = o.id ");
        sql.append("JOIN tblUser u on u.id = s.parent_id ");
        sql.append("WHERE u.id = :userId ");
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", parentId);
        return jdbcTemplate.query(sql.toString(), parameters, (rs, rowNum) ->
                ChooseStudentDTO.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .avatar(rs.getString("avatar"))
                        .dateOfBirth(rs.getTimestamp("date_of_birth"))
                        .className(rs.getString("class_name"))
                        .groupClassName(rs.getString("group_class_name"))
                        .schoolName(rs.getString("school_name"))
                        .build());
    }
}

package site.code4fun.repository.jdbc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.dto.CheckinDTO;

@Repository
public class JCheckinRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<CheckinDTO> getAllCheckin(Long lessionId, List<Long> studentIds, Timestamp createdDate) {
		if(studentIds.size() == 0) return new ArrayList<>();
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentIds", studentIds);
		parameters.addValue("lessionId", lessionId);
		parameters.addValue("createdDate", createdDate);
		
		StringBuilder sql= new StringBuilder("SELECT COUNT(st.id) AS total_checkin, st.class_id,cl.name as class_name, c.lession_id, s.title,");
		sql.append("DATE_FORMAT(c.created_date, '%Y-%m-%d') AS created_date,");
		sql.append("SUM(CASE WHEN c.`present` = TRUE THEN 0 ELSE 1 END) AS total_absent,");
		sql.append("SUM(CASE WHEN c.`present` = TRUE THEN 1 ELSE 0 END) AS total_present ");
		sql.append("FROM tblCheckin c ");
		sql.append("JOIN tblLession s ON c.lession_id = s.id ");
		sql.append("LEFT JOIN tblStudent st ON c.student_id = st.id ");
		sql.append("JOIN tblClass cl ON cl.id = st.class_id ");
		sql.append("WHERE st.id IN (:studentIds) ");
		
		if(lessionId != null) sql.append("AND c.lession_id = :lessionId ");
		if(createdDate != null) sql.append("AND DATE_FORMAT(c.created_date, '%Y-%m-%d') = DATE_FORMAT(:createdDate, '%Y-%m-%d') ");
		sql.append("GROUP BY st.class_id, DATE_FORMAT(c.created_date, '%Y-%m-%d'), c.lession_id");
		return jdbcTemplate.query(sql.toString(), parameters, (rs, numRow) ->
			CheckinDTO.builder()
					.classId(rs.getLong("class_id"))
					.className(rs.getString("class_name"))
					.lessionId(rs.getLong("lession_id"))
					.lessionName(rs.getString("title"))
					.createdDate(rs.getTimestamp("created_date"))
					.totalAbsent(rs.getInt("total_absent"))
					.totalPresent(rs.getInt("total_present"))
					.totalCheckin(rs.getInt("total_checkin"))
					.build()
		);
	}

	public List<CheckinDTO> getDetailCheckin(String studentIds, Long lessionId, Date createdDate){
		String sql = "CALL getCheckin(:studentIds, :lessionId, :createdDate)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentIds", studentIds);
		parameters.addValue("lessionId", lessionId);
		parameters.addValue("createdDate", createdDate);
		return jdbcTemplate.query(sql, parameters, (rs, numRow) ->
				CheckinDTO.builder()
						.classId(rs.getLong("class_id"))
						.lessionId(rs.getLong("lession_id"))
						.studentId(rs.getLong("student_id"))
						.lessionName(rs.getString("title"))
						.present(rs.getBoolean("present"))
						.createdDate(rs.getTimestamp("created_date"))
						.studentCode(rs.getString("student_code"))
						.studentName(rs.getString("name"))
						.className(rs.getString("class_name"))
						.createdName(rs.getString("full_name"))
						.note(rs.getString("note"))
						.build()
		);
	}
}

package site.code4fun.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.dto.CheckinDTO;

@Repository
public class JCheckinRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<CheckinDTO> getAllCheckin(Long lessionId, List<Long> classIds, Timestamp createdDate) {
		if(classIds.size() == 0) return new ArrayList<>();
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("classIds", classIds);
		parameters.addValue("lessionId", lessionId);
		parameters.addValue("createdDate", createdDate);
		
		StringBuilder sql= new StringBuilder("SELECT COUNT(c.id) AS total_checkin, c.class_id, c.lession_id, s.title,");
		sql.append("DATE_FORMAT(c.created_date, '%Y-%m-%d') AS created_date,");
		sql.append("SUM(CASE WHEN c.`present` = FALSE THEN 1 ELSE 0 END) AS total_absent,");
		sql.append("SUM(CASE WHEN c.`present` = TRUE THEN 1 ELSE 0 END) AS total_present ");
		sql.append("FROM tblCheckin c ");
		sql.append("JOIN tblLession s ON c.lession_id = s.id ");
		sql.append("WHERE c.class_id IN (:classIds) ");
		
		if(lessionId != null) sql.append("AND c.lession_id = :lessionId ");
		if(createdDate != null) sql.append("AND DATE_FORMAT(c.created_date, '%Y-%m-%d') = DATE_FORMAT(:createdDate, '%Y-%m-%d') ");
		sql.append("GROUP BY c.class_id, DATE_FORMAT(c.created_date, '%Y-%m-%d'), c.lession_id");
		List<CheckinDTO> lstRes = new ArrayList<>();
		jdbcTemplate.query(sql.toString(), parameters, rs -> {
			CheckinDTO dto = CheckinDTO.builder()
					.classId(rs.getLong("class_id"))
					.lessionId(rs.getLong("lession_id"))
					.lessionName(rs.getString("title"))
					.createdDate(rs.getTimestamp("created_date"))
					.totalAbsent(rs.getInt("total_absent"))
					.totalPresent(rs.getInt("total_present"))
					.totalCheckin(rs.getInt("total_checkin"))
					.build();
			lstRes.add(dto);
		});
		return lstRes;
	}
}

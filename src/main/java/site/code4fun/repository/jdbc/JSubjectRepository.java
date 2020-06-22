package site.code4fun.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.Subject;

@Repository
public class JSubjectRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<Subject> findByClassIds(List<Long> classIds, int limit, int offset) {
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("classIds", classIds);
		parameters.addValue("limit", limit);
		parameters.addValue("offset", offset);
		StringBuilder sql= new StringBuilder("SELECT s.*, c.name as class_name FROM tblSubject s ");
		sql.append("JOIN tblClass c on s.class_id = c.id ");
		sql.append("WHERE s.class_id IN (:classIds) ");
		sql.append("LIMIT :offset, :limit");
		List<Subject> lstRes = new ArrayList<>();
		if(classIds.size() == 0) return lstRes;
		jdbcTemplate.query(sql.toString(), parameters, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				Subject st = Subject.builder()
						.id(rs.getLong("id"))
						.className(rs.getString("class_name"))
						.title(rs.getString("title"))
						.description(rs.getString("description"))
						.endTime(rs.getTimestamp("end_time"))
						.startTime(rs.getTimestamp("start_time"))
						.build();
				lstRes.add(st);
			}
		});
		return lstRes;
	}
}

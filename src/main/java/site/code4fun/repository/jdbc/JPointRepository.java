package site.code4fun.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.dto.PointDTO;

@Repository
public class JPointRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public Map<Long, PointDTO> getPoint(String studentIds, Long subjectId, Byte sem, Byte numOfTest){
		String sql = "Call getListPoint(:studentIds, :subjectId, :sem, :numOfTest)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentIds", studentIds);
		parameters.addValue("subjectId", subjectId);
		parameters.addValue("sem", sem);
		parameters.addValue("numOfTest", numOfTest);
		
		HashMap<Long, PointDTO> mapRes = new HashMap<Long, PointDTO>();
		jdbcTemplate.query(sql, parameters, rs -> {
			PointDTO st = PointDTO.builder()
					.id(rs.getLong("id"))
					.studentId(rs.getLong("student_id"))
					.subjectId(rs.getLong("subject_id"))
					.sem(rs.getByte("sem"))
					.numOfTest(rs.getByte("num_of_test"))
					.pointMulti1(rs.getString("multiple1"))
					.pointMulti2(rs.getString("multiple2"))
					.pointMulti3(rs.getString("multiple3"))
					.build();
			mapRes.put(st.getStudentId(), st);
		});
		return mapRes;
	}
}

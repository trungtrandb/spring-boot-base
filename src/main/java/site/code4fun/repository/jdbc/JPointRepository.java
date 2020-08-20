package site.code4fun.repository.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.dto.PointDTO;

@Repository
public class JPointRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public Map<Long, PointDTO> getPoint(String studentIds, Long subjectId, Byte sem){
		String sql = "Call getListPoint(:studentIds, :subjectId, :sem)";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentIds", studentIds);
		parameters.addValue("subjectId", subjectId);
		parameters.addValue("sem", sem);

		HashMap<Long, PointDTO> mapRes = new HashMap<Long, PointDTO>();
		jdbcTemplate.query(sql, parameters, rs -> {
			PointDTO st = PointDTO.builder()
					.id(rs.getLong("id"))
					.studentId(rs.getLong("student_id"))
					.subjectId(rs.getLong("subject_id"))
					.sem(rs.getByte("sem"))
					.pointMulti1(rs.getString("multiple1"))
					.pointMulti2(rs.getString("multiple2"))
					.pointMulti3(rs.getString("multiple3"))
					.build();
			mapRes.put(st.getStudentId(), st);
		});
		return mapRes;
	}
	
	public List<PointDTO> getPointStudent(Long studentId, Long subjectId, Byte sem){
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("studentId", studentId);
		String sql = "SELECT * FROM tblPoint p JOIN tblSubject s ON p.subject_id = s.id WHERE p.student_id = :studentId ";
		if(null != subjectId) {
			sql += "AND p.subject_id = :subjectId ";
			parameters.addValue("subjectId", subjectId);
		}
		
		if(null != sem) {
			sql += "AND p.sem = :sem";
			parameters.addValue("sem", sem);
		}
		return jdbcTemplate.query(sql, parameters, (rs, numRow) -> 
			PointDTO.builder()
					.id(rs.getLong("id"))
					.studentId(rs.getLong("student_id"))
					.subjectId(rs.getLong("subject_id"))
					.sem(rs.getByte("sem"))
					.point(rs.getFloat("point"))
					.multiple(rs.getByte("multiple"))
					.subjectName(rs.getString("name"))
					.build()
		);
	}
}

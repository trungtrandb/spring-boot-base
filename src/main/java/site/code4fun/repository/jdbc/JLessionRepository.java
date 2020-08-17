package site.code4fun.repository.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.Lession;
import site.code4fun.entity.User;

@Repository
public class JLessionRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Lession> findByClassIds(List<Long> classIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("classIds", classIds);
        StringBuilder sql = new StringBuilder("SELECT ls.*, c.name as class_name, s.name as subject_name FROM tblLession ls ");
        sql.append("JOIN tblClass c on ls.class_id = c.id ");
        sql.append("JOIN tblSubject s ON ls.subject_id = s.id ");
        sql.append("WHERE ls.class_id IN (:classIds) ");
        List<Lession> lstRes = new ArrayList<>();
        if (classIds.size() == 0) return lstRes;
        jdbcTemplate.query(sql.toString(), parameters, rs -> {
            Lession st = Lession.builder()
                    .id(rs.getLong("id"))
                    .className(rs.getString("class_name"))
                    .title(rs.getString("title"))
                    .description(rs.getString("description"))
                    .endTime(rs.getTimestamp("end_time"))
                    .startTime(rs.getTimestamp("start_time"))
                    .subjectId(rs.getLong("subject_id"))
                    .userId(rs.getLong("user_id"))
                    .classId(rs.getLong("class_id"))
                    .subjectName(rs.getNString("subject_name"))
                    .build();
            lstRes.add(st);
        });
        return lstRes;
    }

    public List<User> findUserByClassId(Long classId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("classId", classId);
        StringBuilder sql = new StringBuilder("SELECT u.* FROM tblLession ls LEFT JOIN tblUser u ON u.id = ls.user_id where ls.class_id = :classId GROUP BY u.id");
        return jdbcTemplate.query(sql.toString(), parameters, (rs, numRow) ->
                User.builder()
                        .id(rs.getLong("id"))
                        .fullName(rs.getString("full_name"))
                        .avatar(rs.getString("avatar"))
						.username(rs.getString("user_name"))
                        .build()
        );
    }
}

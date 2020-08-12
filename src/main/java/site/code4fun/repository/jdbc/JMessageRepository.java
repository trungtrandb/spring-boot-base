package site.code4fun.repository.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.OutputMessage;

@Repository
public class JMessageRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<OutputMessage> getListConversion(String userName){
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("userName", userName);
		StringBuilder sql= new StringBuilder("CALL getConversionByUserName(:userName)");
		List<OutputMessage> lstRes = new ArrayList<>();
		jdbcTemplate.query(sql.toString(), parameters, rs -> {
			OutputMessage st = OutputMessage.builder()
					.id(rs.getLong("id"))
					.avatar(rs.getString("avatar"))
					.from(rs.getString("send_from"))
					.fromId(rs.getLong("send_from_id"))
					.to(rs.getString("send_to"))
					.text(rs.getString("text"))
					.createdDate(rs.getTimestamp("created_date"))
					.fullName(rs.getString("full_name"))
					.totalMessage(rs.getInt("total_message"))
					.build();
			lstRes.add(st);
		});
		return lstRes;
	}
	
	public List<OutputMessage> getMessage(String fromUser, String toUser, Integer limit, Long offset){
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("limit", limit);
		parameters.addValue("offset", offset);
		parameters.addValue("fromUser", fromUser);
		parameters.addValue("toUser", toUser);
		
		StringBuilder sql= new StringBuilder("CALL getMessage(:fromUser, :toUser, :limit, :offset)");
		List<OutputMessage> lstRes = new ArrayList<>();
		jdbcTemplate.query(sql.toString(), parameters, rs -> {
			OutputMessage st = OutputMessage.builder()
					.id(rs.getLong("id"))
					.avatar(rs.getString("avatar"))
					.from(rs.getString("send_from"))
					.fromId(rs.getLong("send_from_id"))
					.to(rs.getString("send_to"))
					.text(rs.getString("text"))
					.createdDate(rs.getTimestamp("created_date"))
					.fullName(rs.getString("full_name"))
					.build();
			lstRes.add(st);
		});
		return lstRes;
	}

}

package site.code4fun.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import site.code4fun.entity.dto.NotifyDTO;

public class NotifyDTOMapper implements RowMapper<NotifyDTO>{

	@Override
	public NotifyDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		return NotifyDTO.builder()
				.id(rs.getLong("id"))
				.title(rs.getString("title"))
				.deviceToken(rs.getString("device_token"))
				.content(rs.getString("content"))
				.createdName(rs.getString("full_name"))
				.status(rs.getString("status"))
				.createdDate(rs.getTimestamp("created_date"))
				.isRead(rs.getBoolean("is_read"))
				.build();
	}

}

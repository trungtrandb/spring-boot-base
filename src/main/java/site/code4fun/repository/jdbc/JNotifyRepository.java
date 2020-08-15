package site.code4fun.repository.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.NotifyDevice;
import site.code4fun.entity.dto.NotifyDTO;
import site.code4fun.mapper.NotifyDTOMapper;

@Repository
public class JNotifyRepository {
	
	 @Autowired
	    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<NotifyDTO> getNotifyOfDevicePending() { // Chỉ dùng cho queue schedule notify
        String sql = "SELECT n.*, nd.device_token, null as full_name " + "FROM tblNotify n " +
                "JOIN tblNotifyDevice nd ON n.id = nd.notify_id " +
                "WHERE nd.status IN ('PENDING') AND nd.is_read != 1";
        return jdbcTemplate.query(sql, new NotifyDTOMapper());
    }
	    public int updateNoti(NotifyDevice notiDevice) {
	        String sql = "UPDATE tblNotifyDevice SET status =:status, note =:note,  updated_date = NOW() WHERE notify_id = :notifyId AND device_token = :deviceToken";
	        MapSqlParameterSource parameters = new MapSqlParameterSource();
	        parameters.addValue("status", notiDevice.getStatus());
	        parameters.addValue("note", notiDevice.getNote());
	        parameters.addValue("notifyId", notiDevice.getNotifyId());
	        parameters.addValue("deviceToken", notiDevice.getDeviceToken());
	        return jdbcTemplate.update(sql, parameters);
	    }

	    public List<NotifyDTO> getNotifyByOrg(Long orgId) {
	        String sql = "Select n.*, u.full_name, null as device_token from tblNotify n JOIN tblUser u ON n.created_by = u.id WHERE n.school_id = :schoolId ORDER BY n.created_date DESC";
	        MapSqlParameterSource parameters = new MapSqlParameterSource();
	        parameters.addValue("schoolId", orgId);
	        return jdbcTemplate.query(sql, parameters, new NotifyDTOMapper());
	    }

	    public List<NotifyDTO> getNotifyByUserId(Long Id, Integer start, Long offset) {
	        StringBuilder sb = new StringBuilder("SELECT n.*, nd.user_id, nd.is_read, u.full_name, null as device_token, n.title, n.content from tblNotify n ");
	        sb.append("JOIN tblNotifyDevice nd ON n.id = nd.notify_id ");
	        sb.append("JOIN tblUser u ON u.id = n.created_by ");
	        sb.append("WHERE user_id = :userId ");
	        sb.append("GROUP BY notify_id, user_id ");
	        sb.append("ORDER BY n.created_date DESC ");
	        MapSqlParameterSource parameters = new MapSqlParameterSource();
	        if (null != start && null != offset) {
	            sb.append("LIMIT :offset, :start");
	            parameters.addValue("start", start);
	            parameters.addValue("offset", offset);
	        }
	        parameters.addValue("userId", Id);
	        return jdbcTemplate.query(sb.toString(), parameters, (rs, rowNum) ->
	                NotifyDTO.builder()
	                        .id(rs.getLong("id"))
	                        .title(rs.getString("title"))
	                        .deviceToken(rs.getString("device_token"))
	                        .content(rs.getString("content"))
	                        .createdName(rs.getString("full_name"))
	                        .status(rs.getString("status"))
	                        .isRead(rs.getBoolean("is_read"))
	                        .createdDate(rs.getTimestamp("created_date"))
	                        .build()
	        );
	    }
}

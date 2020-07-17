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
	
	public List<NotifyDTO> getNotifyOfDevicePending(){
		StringBuilder sql = new StringBuilder("SELECT n.*, nd.device_token ");
		sql.append("FROM tblNotify n ");
		sql.append("JOIN tblNotifyDevice nd ON n.id = nd.notify_id ");
		sql.append("WHERE nd.status IN ('PENDING', 'ERROR')");
		return jdbcTemplate.query(sql.toString(), new NotifyDTOMapper());
	}

	public int updateNoti(NotifyDevice notiDevice) {
		String sql = "UPDATE tblNotifyDevice SET status =:status, note =:note WHERE notify_id = :notifyId AND device_token = :deviceToken";
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("status", notiDevice.getStatus());
		parameters.addValue("note", notiDevice.getNote());
		parameters.addValue("notifyId", notiDevice.getNotifyId());
		parameters.addValue("deviceToken", notiDevice.getDeviceToken());
		return jdbcTemplate.update(sql, parameters);
	}

}

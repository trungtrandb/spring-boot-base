package site.code4fun.repository.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import site.code4fun.entity.User;
import site.code4fun.mapper.UserMapper;

@Repository
public class JUserOrganizationRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public List<User> getTeachersByOrgIds(List<Long> organizationIds){
		if(organizationIds.size() == 0) return new ArrayList<>();
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("organizationIds", organizationIds);
		
		StringBuilder sql = new StringBuilder("Select * FROM tblUser u");
		sql.append(" JOIN tblUserOrganization uo ON u.id = uo.user_id"); 
		sql.append(" WHERE uo.organization_id IN (:organizationIds)");
		return jdbcTemplate.query(sql.toString(), parameters, new UserMapper());
	}

}

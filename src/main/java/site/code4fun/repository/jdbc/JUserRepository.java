package site.code4fun.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JUserRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

}

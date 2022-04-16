package site.code4fun.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import site.code4fun.entity.UserPrincipal;
import site.code4fun.entity.dto.AccessTokenResponseDTO;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
	private static final long serialVersionUID = -2550185165626007488L;
	public static final int JWT_TOKEN_VALIDITY = 5 * 3600;
	private static final String SECRET = "2550185165626007488L";

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public AccessTokenResponseDTO generateToken(UserPrincipal user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("fullName", user.getFullName());
		claims.put("role", user.getRole());
		claims.put("avatar", user.getAvatar());
		
		String token =  Jwts.builder()
				.setClaims(claims)
				.setSubject(user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		
		return AccessTokenResponseDTO.builder()
				.token(token)
				.build();
	}


	public Boolean isValidToken(String token) {
		return !isTokenExpired(token);
	}
	
	public UserPrincipal getUserPrincipalFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		
		return UserPrincipal.builder()
				.id(((Number) claims.get("id")).longValue())
				.authorities(Collections.singletonList(new SimpleGrantedAuthority(claims.get("role").toString())))
				.role(claims.get("role").toString())
				.username(claims.getSubject())
				.build();
	}
}

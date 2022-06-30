package site.code4fun.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import site.code4fun.dto.AccessTokenResponseDTO;
import site.code4fun.model.Role;
import site.code4fun.model.User;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtTokenUtil implements Serializable {
	private static final long serialVersionUID = -2550185165626007488L;
	public static final int JWT_TOKEN_VALIDITY = 5 * 3600;
	private static final String SECRET = "2550185165626007488L";
	private final ModelMapper modelMapper = new ModelMapper();

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

	public boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public AccessTokenResponseDTO generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", user.getId());
		claims.put("fullName", user.getFullName());
		claims.put("roles", user.getRoles());
		claims.put("authorities", user.getAuthorities());
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

	@SuppressWarnings("unchecked")
	public User getUserPrincipalFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
		Set<Role> lstRole = new HashSet<>();
		List<GrantedAuthority> lstAuthorities = new ArrayList<>();
		if (null != claims.get("roles")){
			lstRole = modelMapper.map(claims.get("roles"), new TypeToken<Set<Role>>() {}.getType());
		}

		if (null != claims.get("authorities")){
			ArrayList<Map<String, String>> lst = modelMapper.map(claims.get("authorities"), ArrayList.class);
			lst.forEach(_item ->{
				Map<String, String> mapVal = modelMapper.map(_item, HashMap.class);
				lstAuthorities.add(new SimpleGrantedAuthority(mapVal.get("authority")));
			});
		}

		return User.builder()
				.id(((Number) claims.get("id")).longValue())
				.authorities(lstAuthorities)
				.roles(lstRole)
				.username(claims.getSubject())
				.build();
	}
}

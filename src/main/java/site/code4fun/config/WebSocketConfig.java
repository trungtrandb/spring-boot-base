package site.code4fun.config;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import site.code4fun.util.JwtTokenUtil;
import site.code4fun.util.StringUtils;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
        te.setPoolSize(1);
        te.setThreadNamePrefix("wss-heartbeat-thread-");
        te.initialize();
        
    	config.enableSimpleBroker("/queue/", "/topic/")
    		.setHeartbeatValue(new long[]{0, 0})
    		.setTaskScheduler(te);;
    	config.setApplicationDestinationPrefixes("/app");
    	config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").addInterceptors(getInterceptor()).setAllowedOrigins("*").withSockJS(); // Đăng ký enpoint khởi tạo ws
        registry.addEndpoint("/ws-test").addInterceptors(getPublicInterceptor()).setAllowedOrigins("*").withSockJS();
    }
    
    private HandshakeInterceptor getInterceptor() {
        return new HandshakeInterceptor() {	
			@Override
			public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
					Map<String, Object> attributes) throws Exception {	
				System.out.println("Has New connection");
				String jwtToken = "";
				List<String> cookieHeader = request.getHeaders().get("Cookie");
				if(cookieHeader != null) {
					String rawCookie = cookieHeader.get(0);
					String[] listCookieParam = rawCookie.split(";");
					for(String cookieParam :listCookieParam)
					{
						String[] rawCookieNameAndValuePair = cookieParam.split("=");
						String key = rawCookieNameAndValuePair[0].trim();
						if(key.equalsIgnoreCase("Authorization")) jwtToken = rawCookieNameAndValuePair[1].trim();
					}
				}
				
				String requestParam = request.getURI().getQuery();
				if(!StringUtils.isNull(requestParam)) {
					String[] rawNameAndValuePair = requestParam.split("=");
					String key = rawNameAndValuePair[0].trim();
					if(key.equalsIgnoreCase("Authorization")) jwtToken = rawNameAndValuePair[1].trim();
				}

			    if(!StringUtils.isNull(jwtToken) && !jwtTokenUtil.isTokenExpired(jwtToken)) {
					UserDetails userDetails = jwtTokenUtil.getUserPrincipalFromToken(jwtToken);
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
					return true;
				}	
			    return false;
			}
			
			@Override
			public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
					Exception exception) {				
			}
		};
    }
    
    private HandshakeInterceptor getPublicInterceptor() {
        return new HandshakeInterceptor() {	
			@Override
			public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
					Map<String, Object> attributes) throws Exception {	
				System.out.println("Has New connection public");
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("trungtrandb1@gmail.com", "123456");
				Authentication authentication = authenticationManager.authenticate(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				return true;
			}
			
			@Override
			public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
					Exception exception) {				
			}
		};
    }
}
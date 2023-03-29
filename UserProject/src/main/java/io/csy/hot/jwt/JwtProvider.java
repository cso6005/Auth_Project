package io.csy.hot.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Value("${spring.jwt.token.key}")
	private String key;

	@Value("${spring.jwt.live.atk}")
	private Long atkLive;
	
    @Value("${spring.jwt.live.rtk}")
    private Long rtkLive;
	
	@Value("${spring.jwt.issuer}")
	private String issuer;
	
	@PostConstruct
	protected void init() {
		key = Base64.getEncoder().encodeToString(key.getBytes());
	}
	
	public TokenResponse createTokenByLogin(Authentication authentication) throws JsonProcessingException {
		
		
		String accountEmail = authentication.getName();
		
		String authorities = authentication.getAuthorities().stream().map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.joining(","));

		Subject atkSubject = Subject.atk(accountEmail, authorities);

		String atk = createToken(atkSubject, atkLive);
						
		return new TokenResponse(atk, null, null);
		
	}
	
	private String createToken(Subject subject, Long tokenLive) throws JsonProcessingException {
		
		String subjectStr = objectMapper.writeValueAsString(subject);// 객체 -> Json 문자열
		
		Claims claims = Jwts.claims().setIssuer(issuer).setSubject(subjectStr);
		
		long now = (new Date()).getTime();
		
		Date accessTokenExpiresIn = new Date(now + tokenLive); 

		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(now))
				.setExpiration(accessTokenExpiresIn)
				.signWith(SignatureAlgorithm.HS256, key).compact();
		
	}
	
	// atk 로 jwt 의 payload 에 있는 유저 정보 Subject 로 꺼내기 // 계정id, 계정유형, 토큰유형 정보
	public Subject getSubject(String atk) throws JsonProcessingException {
		String subjectStr = Jwts.parser().setSigningKey(key).parseClaimsJws(atk).getBody().getSubject();
		
		return objectMapper.readValue(subjectStr, Subject.class);
	}
	

}

package io.csy.hot.jwt;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.csy.hot.common.RedisDAO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtProvider {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final RedisDAO redisDAO;

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
		String authorities = authentication.getAuthorities().stream()
				.map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.joining(",")).substring(5);

		// 클레임에 들어갈 유저 정보객체
		Subject atkSubject = Subject.atk(accountEmail, authorities);
		Subject rtkSubject = Subject.rtk(accountEmail, authorities);

		String atk = createToken(atkSubject, atkLive);
		String rtk = createToken(rtkSubject, rtkLive);

		// rtk 저장
		redisDAO.setValues(accountEmail, rtk, Duration.ofMillis(rtkLive));

		// rtk 를 응답할까 말까,,
		return new TokenResponse(atk, rtk, authorities);
	}

	private String createToken(Subject subject, Long tokenLive) throws JsonProcessingException {

		String subjectStr = objectMapper.writeValueAsString(subject);// 객체 -> Json 문자열
		Claims claims = Jwts.claims().setIssuer(issuer).setSubject(subjectStr);
		long now = (new Date()).getTime();
		Date accessTokenExpiresIn = new Date(now + tokenLive);

		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(now)).setExpiration(accessTokenExpiresIn)
				.signWith(SignatureAlgorithm.HS256, key).compact();

	}

	// atk 로 jwt 의 payload 에 있는 유저 정보 Subject 로 꺼내기 // 계정id, 계정유형, 토큰유형 정보
	public Subject getSubject(String atk) throws JsonProcessingException {

		try {
			System.out.println("토큰 확인");
			String subjectStr = Jwts.parser().setSigningKey(key).parseClaimsJws(atk).getBody().getSubject();
			System.out.println("토큰 확인 완료");
			return objectMapper.readValue(subjectStr, Subject.class);

		} catch (SecurityException e) {

			throw new JwtException("잘못된 JWT 시그니처");

		} catch (MalformedJwtException e) {

			throw new JwtException("변조된 JWT 토큰");

		} catch (ExpiredJwtException e) {

			throw new JwtException("토큰 기한 만료");

		} catch (UnsupportedJwtException e) {

			throw new JwtException("Unsupported JWT token.");

		} catch (IllegalArgumentException e) {

			throw new JwtException("JWT token compact of handler are invalid.");
		} catch (SignatureException e){

            throw new JwtException("잘못된 토큰입니다.");
        }


	}

	public TokenResponse reissueATK(AccountDetails accountDetails) throws JsonProcessingException {

		String accountEmail = accountDetails.getUsername();
		String authorities = accountDetails.getAuthorities().stream()
				.map(grantedAuthority -> grantedAuthority.getAuthority()).collect(Collectors.joining(",")).substring(5);

		Subject atkSubject = Subject.atk(accountEmail, authorities);

		String atk = createToken(atkSubject, atkLive);

		return new TokenResponse(atk, null, authorities);
	}
}

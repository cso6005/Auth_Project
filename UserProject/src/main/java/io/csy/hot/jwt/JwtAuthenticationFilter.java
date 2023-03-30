package io.csy.hot.jwt;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.csy.hot.common.RedisDAO;
import io.csy.hot.exception.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	private final AccountDetailsService accountDetailsService;

	private final RedisDAO redisDao;

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");
		String requestURI = request.getRequestURI();

		if (StringUtils.hasText(authorization)) {
			String token = authorization.substring(7); // 만약, 여기에서 에러가 난다면, 바로 authenticationException 으로 customAuthenticationEntry 로 넘어감. 예측 가능한, 제어가 필요한 예외를 제외하고 모든 인증 에러는 그냥 401 인증 실패로 응답하자. 

				Subject subject = jwtProvider.getSubject(token); // jwt 토큰 관련 에러가 나는 포인트로 잡고 여기서 예외 잡음.

				if (subject.getTokenType().equals("RTK") && requestURI.equals("/auth/reissue")) {

					String rtkReids = redisDao.getValues(subject.getAccountEmail());

					if (Objects.isNull(rtkReids) || !rtkReids.equals(token)) {
						throw new JwtException("redis 저장소 rtk 또는 클라이언트 rtk 가 올바르지 않습니다. 확인 바랍니다. ");
					}
				}

				UserDetails userDetails = accountDetailsService.loadUserByUsername(subject.getAccountEmail());

				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
						userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication); // 정상 토큰이면 인증 객체를 SecurityContext에

		}
		filterChain.doFilter(request, response);
	}

}

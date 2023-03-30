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
			String token = authorization.substring(7);

				Subject subject = jwtProvider.getSubject(token);

				if (subject.getTokenType().equals("RTK") && requestURI.equals("/auth/reissue")) {

					String rtkReids = redisDao.getValues(subject.getAccountEmail());

					if (Objects.isNull(rtkReids) || !rtkReids.equals(token)) {
						throw new JwtException("토큰 재발급 도중 예외 발생");
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

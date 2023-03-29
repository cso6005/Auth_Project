package io.csy.hot.jwt;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	private final AccountDetailsService accountDetailsService;

	public JwtAuthenticationFilter(JwtProvider jwtProvider, AccountDetailsService accountDetailsService) {
		this.jwtProvider = jwtProvider;
		this.accountDetailsService = accountDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authorization = request.getHeader("Authorization");

		if (StringUtils.hasText(authorization)) {
			String atk = authorization.substring(7);

			try {

				Subject subject = jwtProvider.getSubject(atk);

				String requestURI = request.getRequestURI();

				if (subject.getTokenType().equals("RTK") && !requestURI.equals("/auth/reissue")) {

					throw new JwtException("잘못된 토큰 입니다.");
				}

				UserDetails userDetails = accountDetailsService.loadUserByUsername(subject.getAccountEmail());

				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
						userDetails.getAuthorities());

				SecurityContextHolder.getContext().setAuthentication(authentication); // 정상 토큰이면 SecurityContext에 저장

			} catch (JwtException ex) {
				System.out.println("토큰 있긴 한데, 틀린 토큰임.!!!!!");
			}
		}
		filterChain.doFilter(request, response);
	}
}

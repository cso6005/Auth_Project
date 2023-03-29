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

// 기본적으로 Filter로 수행되는 것은 Form기반의 아이디와 비밀번호로 진행되는 UsernamePasswordAuthenticationFilter가 수행
// CustomFilter (UsernamePasswordAuthenticationFilter보다 먼저 걸리도록)
// UsernamePasswordAuthenticationToken:  username, password를 쓰는 form기반 인증을 처리하는 필터.
// AuthenticationManager를 통한 인증 실행
// 성공하면, Authentication 객체를 SecurityContext에 저장 후 AuthenticationSuccessHandler 실행
// 실패하면, AuthenticationFailureHandler 실행

// OncePerRequestFilter 를 상속받음
// why? 모든 서블릿에 일관된 요청을 처리하기 위한 필터 (사용자의 한번에 요청 당 딱 한번!) (의도치 않은 경우) 매번 Filter의 내용이 수행되는 것을 방지하기 위해 GenericFilterBean을 상속한 OncePerRequestFilter
// OncePerRequestFilter를 상속하여 구현한 경우 doFilter 대신 doFilterInternal 메서드를 구현하면 된다.
//https://luvstudy.tistory.com/79

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
		System.out.println("doFilterInternal - " + authorization);

		// null이 아니고, 빈문자열도 아니며, 공백으로만 이루어져 있는 문자열도 아닌 문자열인 경우에만 true를 retrun
		if (StringUtils.hasText(authorization)) {
			String atk = authorization.substring(7);
			System.out.println(atk);

			try {

				// atk 로 유저 정보 들고 오기
				Subject subject = jwtProvider.getSubject(atk);

				System.out.println(subject);
				String requestURI = request.getRequestURI();

				if (subject.getTokenType().equals("RTK") && !requestURI.equals("/auth/reissue")) {

					throw new JwtException("잘못된 토큰 입니다.");
//	    			throw new CustomException(UserErrorCode.INVALID_TOKEN_TYPE); // /jwtException - RuntimeException
				}

				// loadUserByUsername 으로 UserDetails 유저 객체 조회 (AccountDetail): 사용자 이메일, 패스워드, 회원
				// 유형(일반 유저, 관리자) 정보가 담겨져 있음.
				UserDetails userDetails = accountDetailsService.loadUserByUsername(subject.getAccountEmail());

				// Authentication 인증 객체 란?
				// 사용자의 인증 정보를 저장하는 토큰 개념으로 사용된다.
				/*
				 * 객체 구조? 1. principal User객체를 저장 - userDetails(AccountDetail) 2. credentials
				 * 사용자 비밀번호 - "" 3. authorities 인증된 사용자의 권한 목록 - userDetails.getAuthorities() 4.
				 * details 인증 부가 정보 5. Authednticated 인증 여부(Bool)
				 * 				

				 */
				System.out.println("필터링");
				System.out.println(userDetails.getAuthorities());
				Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "",
						userDetails.getAuthorities());

				// SecurityContextHolder : SecurityContext 객체를 저장하고 감싸고 있는 wrapper 클래스
				// SecurityContext : Authentication 객체를 어디서든 꺼내어 쓸 수 있도록 제공되는 클래스
				// SecurityContextHolder는 ThreadLocal에 저장되기 때문에 각기 다른 쓰레드별로 다른
				// SecurityContextHolder 인스턴스를 가지고 있어서 사용자 별로 각기 다른 인증 객체를 가질 수 있다.
				// ThreadLocal: Thread마다 할당된 고유 공간(공유X) -> 다른 쓰레드로부터 안전

				// 최종 인증 결과 즉, Authentication 인증 객체 를 SecurityContext에 저장한다. 저장한 인증 객체를 전역적으로 참조
				// 가능하다.
				System.out.println(authentication);
				SecurityContextHolder.getContext().setAuthentication(authentication); // 정상 토큰이면 SecurityContext에 저장
				System.out.println(SecurityContextHolder.getContext().getAuthentication());

			} catch (JwtException ex) {
				// request.setAttribute("", ex.getMessage());
				System.out.println("토큰 있긴 한데, 틀린 토큰임.!!!!!");

			}

		}
		// 토큰 없는 url 은 필터 통과
		filterChain.doFilter(request, response);

	}

}

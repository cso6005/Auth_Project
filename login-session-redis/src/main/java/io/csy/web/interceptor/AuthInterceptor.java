package io.csy.web.interceptor;

import static io.csy.domain.dto.SecurityConstants.KEY_ROLE;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.csy.domain.dto.RoleEnum;
import io.csy.web.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/* 스프링 지원 인터셉터로  URI 요청, 응답 시점을 가로채서 전/후 처리 =>  Interceptor 시점에 Spring Context와 Bean에 접근할 수 
 * WebMvcConfigurer를 구현해 Spring Boot에서 기본적으로 제공해주는 설정 중 interceptor부분을 커스텀해야 함.
 * */

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		log.info("Auth - preHandle");

		System.out.println(request.getSession().getAttribute(KEY_ROLE));

		if (request.getSession().getAttribute(KEY_ROLE) != null
				&& request.getSession().getAttribute(KEY_ROLE).equals(RoleEnum.USER.name())) { // USER 권한이 세션 데이터에 담겨져
																								// 있어야 함.
			return true;
		} else {
			throw new CustomException();
		}

	}
}

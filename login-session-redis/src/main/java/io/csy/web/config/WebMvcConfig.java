package io.csy.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.csy.web.interceptor.AuthInterceptor;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

	private final AuthInterceptor authInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(authInterceptor)
				.addPathPatterns("/api/v1/movie/**") //인터셉터 걸림. (인증, 인가 통과한 사용자만 접근)
				.excludePathPatterns("/api/v1/login/**"); // 로그인은 인터셉터 통과 (인증 상관없이)
	}

}

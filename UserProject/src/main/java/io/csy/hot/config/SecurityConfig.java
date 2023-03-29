package io.csy.hot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.csy.hot.jwt.AccountDetailsService;
import io.csy.hot.jwt.JwtAuthenticationFilter;
import io.csy.hot.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity  
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtProvider jwtProvider;
	private final AccountDetailsService accountDetailsService;
	
	private final CustomAccessDeniedHandler customAccessDeniedHandler;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Bean
	public PasswordEncoder passwordEncoder() {

		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}
	

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.cors().and().csrf().disable()
		.exceptionHandling()
		.accessDeniedHandler(customAccessDeniedHandler)
		.authenticationEntryPoint(customAuthenticationEntryPoint)
		.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
        http
        .authorizeRequests()
        .antMatchers("/board/test").hasRole("USER")
            .antMatchers("/auth/**").permitAll()
            .anyRequest().authenticated();	
		
        http.addFilterBefore(new JwtAuthenticationFilter(jwtProvider, accountDetailsService),
				UsernamePasswordAuthenticationFilter.class);
		

		return http.build();
	}

}

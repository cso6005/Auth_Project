package io.csy.hot.jwt;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.csy.hot.exception.CommonErrorCode;
import io.csy.hot.exception.ErrorResponse;
import io.jsonwebtoken.JwtException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {
	
	public final ObjectMapper objectMapper = new ObjectMapper();


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		try {
			System.out.println("예외 확인");
			
			filterChain.doFilter(request, response); // JwtAuthenticationFilter로 이동
			
		} catch (JwtException ex) {
			// JwtAuthenticationFilter에서 예외 발생하면 바로 setErrorResponse 호출
            setErrorResponse(request, response, ex);
		}
		
		
	}
	
    public void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws IOException {
        
    	System.out.println("예외 응답 메시지 로직");
		String timestamp = new Date().toString();
		
		System.out.println(ex.getMessage());
		System.out.println(ex.getLocalizedMessage());
		System.out.println(ex.getCause());
		System.out.println(ex.getClass());
		
		CommonErrorCode errorCode = errorCode(request.getRequestURI());
		response.setStatus(errorCode.getHttpStatus().value());
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
                
        ErrorResponse errorMessage = new ErrorResponse(timestamp, errorCode.getHttpStatus().value(), errorCode.name(), errorCode.getMessage() + ex.getMessage() , request.getRequestURI());
    	
        String res = this.convertObjectToJson(errorMessage);
        
    	response.getWriter().print(res);
    }	
    
    
    private String convertObjectToJson(Object object) throws JsonProcessingException {
        
    	return object == null ? null : objectMapper.writeValueAsString(object);
    	
    }
    
    private CommonErrorCode errorCode(String requestURI) {
    	
		if (requestURI.equals("/auth/reissue")) {
			return CommonErrorCode.REISSUE_JWT_ERROR;
		} else {
			return  CommonErrorCode.JWT_ERROR;
		}

    }

}

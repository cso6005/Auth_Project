package io.csy.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.csy.exception.CommonErrorCode;
import io.csy.exception.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            	
		String timestamp = new Date().toString();
		
		CommonErrorCode errorCode = errorCode(request.getRequestURI());
		System.out.println("메시지");
		System.out.println(authException.getMessage());
				
		response.setStatus(errorCode.getHttpStatus().value());
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
        
        ErrorResponse errorMessage = new ErrorResponse(timestamp, errorCode.getHttpStatus().value(), errorCode.name(), errorCode.getMessage(), request.getRequestURI());
        	    	
        String res = this.convertObjectToJson(errorMessage);
        
    	response.getWriter().print(res);
    	
//        response.setCharacterEncoding("utf-8");
//        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.getWriter().write(objectMapper.writeValueAsString(Response.builder()
//                .status(HttpStatus.UNAUTHORIZED.value())
//                .message(message)
//                .build()));
    	
    }
    
    private CommonErrorCode errorCode(String requestURI) {
    	
		if (requestURI.equals("/auth/reissue")) {
			return CommonErrorCode.REISSUE_AUTH_ERROR;
		} else {
			return  CommonErrorCode.AUTH_ERROR;
		}

    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        
    	return object == null ? null : objectMapper.writeValueAsString(object);
    	
    }
}

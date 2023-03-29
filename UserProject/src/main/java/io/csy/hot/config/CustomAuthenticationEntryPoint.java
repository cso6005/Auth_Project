package io.csy.hot.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.csy.hot.exception.CommonErrorCode;
import io.csy.hot.exception.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import springfox.documentation.service.ResponseMessage;

@Getter
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
            	
		CommonErrorCode errorCode = CommonErrorCode.AUTH_ERROR;
		String timestamp = new Date().toString();
    	
		response.setStatus(errorCode.getHttpStatus().value());
    	response.setContentType("application/json");
    	response.setCharacterEncoding("UTF-8");
        
        ErrorResponse message = new ErrorResponse(timestamp, errorCode.getHttpStatus().value(), errorCode.name(), errorCode.getMessage(), request.getRequestURI());
        		
        String res = this.convertObjectToJson(message);
 
    	response.getWriter().print(res);
    	
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        
    	return object == null ? null : objectMapper.writeValueAsString(object);
    	
    }
}

package io.csy.hot.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.csy.hot.exception.CommonErrorCode;
import io.csy.hot.exception.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public 	void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException, ServletException {


		CommonErrorCode errorCode = CommonErrorCode.ACCESS_DENIED;
//		LOGGER.warn("HandleAccessDeniedException:CLIENT REQUEST ERROR:INVALID_AUTH_TOKEN: {}", ex.getMessage());
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

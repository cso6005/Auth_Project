package io.csy.hot.exception;

import java.nio.file.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
	// 사용자 정의 예외처리
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Object> customExceptionHandler(CustomException ex, HttpServletRequest request) {

		UserErrorCode errorCode = ex.getErrorCode();
		
		return ErrorResponse.toUserErrorResponse(errorCode, request.getRequestURI());

	}

	// 400 	
	@ExceptionHandler(IllegalArgumentException.class) //대개 메소드의 매개변수 유형을 잘 못 사용하는 경우
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
		
		CommonErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		LOGGER.warn("HandleIllegalArgumentException:CLIENT REQUEST ERROR:INVALID_PARAMETER: {}", ex.getMessage());
		return ErrorResponse.toCommonErrorResponse(errorCode, request.getRequestURI());
	
	}
	
    @Override    
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
   	
    	CommonErrorCode errorCode = CommonErrorCode.MISSING_PARAMETER;
		LOGGER.warn("HandleHttpRequestMethodNotSupportedException:CLIENT REQUEST ERROR:MISSING_PARAMETER: {}", ex.getMessage());

		return ErrorResponse.toCommonErrorResponse(errorCode, request.getDescription(false).substring(4));
						
    }
	

    // 401 
    // AccessDeniedHandler는 서버에 요청을 할 때 액세스가 가능한지 권한을 체크후 액세스 할 수 없는 요청을 했을시 동작된다.
    // 만약, securityConfig 에서 사용자 유형에 따라 권한이 다르게 설정 할 경우에,  user 계정이 관리자 자원 요청에 시도한 경우에 발생하게 된다.
    // 여기서 하고싶다면, HandlerExceptionResolver 를 사용해야 함.
    
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
//
//		CommonErrorCode errorCode = CommonErrorCode.INVALID_AUTH_TOKEN;
//		LOGGER.warn("HandleAccessDeniedException:CLIENT REQUEST ERROR:INVALID_AUTH_TOKEN: {}", ex.getMessage());
//
//		return ErrorResponse.toCommonErrorResponse(errorCode, request.getRequestURI());
//		
//    }
    
    // 로그인 시, 존재하지 않은 계정일 때, -> 아이디, 비번 틀릴 때 모두
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
    	
    	UserErrorCode errorCode = UserErrorCode.INVALID_LOGIN_PARAMETER;
    	LOGGER.info("handleBadCredentialsException: INVALID_LOGIN_PARAMETER : {} - {} ", ex.getMessage() , errorCode.getMessage());
    	
    	return ErrorResponse.toUserErrorResponse(errorCode, request.getRequestURI());
    }
    
    
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<Object> resourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
//		
//    	CommonErrorCode errorCode = CommonErrorCode.RESOURCE_NOT_FOUND;
//
//      return ErrorResponse.toCommonErrorResponse(errorCode, request.getRequestURI());
//    }
    
    
    // 405
    @Override    
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {		
    	
    	CommonErrorCode errorCode = CommonErrorCode.INVALID_METHOD;
		LOGGER.warn("HandleHttpRequestMethodNotSupportedException:CLIENT REQUEST ERROR:INVALID_METHOD: {}", ex.getMessage());

		return ErrorResponse.toCommonErrorResponse(errorCode, request.getDescription(false).substring(4));
						
    }
    
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    	CommonErrorCode errorCode = CommonErrorCode.METHOD_ARGUMENT_NOT_VALID;
    	String detailMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    	
		LOGGER.warn("HandleHttpRequestMethodNotSupportedException:CLIENT REQUEST ERROR:INVALID_METHOD: {}", ex.getMessage());

		return ErrorResponse.toDataVaildErrorResponse(errorCode, detailMessage, request.getDescription(false).substring(4));	}
    
    
	//그 외 서버 에러 500
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> exceptionHandler(Exception ex, HttpServletRequest request ) {
		
		CommonErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		LOGGER.warn("HandleException: SERVER ERROR :{}", ex.getMessage()); // 이 에러 메시지를 클라이언트에게 보낼 필요가 없다 판단.! 로그에만 찍어두기

		return ErrorResponse.toCommonErrorResponse(errorCode, request.getRequestURI());
		
	}
	
	
	
/* 오버라이딩 해야 하는 것들 !!
 * 
 * 			HttpRequestMethodNotSupportedException.class, 
			HttpMediaTypeNotSupportedException.class,
			HttpMediaTypeNotAcceptableException.class,
			MissingPathVariableException.class,
			MissingServletRequestParameterException.class,
			ServletRequestBindingException.class,
			ConversionNotSupportedException.class,
			TypeMismatchException.class,
			HttpMessageNotReadableException.class,
			HttpMessageNotWritableException.class,
			MethodArgumentNotValidException.class,
			MissingServletRequestPartException.class,
			BindException.class,
			NoHandlerFoundException.class,
			AsyncRequestTimeoutException.class
 * */

}
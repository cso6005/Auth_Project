package io.csy.hot.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {
	

		//400
		INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터가 존재합니다. 확인 후 다시 시도해주세요."),
		MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "메서드 매개 변수 유형에 필요한 요청 매개 변수 'id'가 없습니다."),
	     
	    //401 // AuthenticationException
	    AUTH_ERROR(HttpStatus.UNAUTHORIZED, "ATK - 인증 실패 "),
	    
	    JWT_ERROR(HttpStatus.UNAUTHORIZED, "ATK - JWT 토큰 관련 에러  "),
	    
	    REISSUE_AUTH_ERROR(HttpStatus.UNAUTHORIZED, "RTK  - 인증 실패"),

	    REISSUE_JWT_ERROR(HttpStatus.UNAUTHORIZED, "RTK  - JWT 토큰 관련 에러 "),

	    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다. "),
	    
	    //404 // 사실 상 post 가 없으므로 빼도 됨.
	    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "리소스가 존재하지 않습니다. 확인 후 다시 시도해주세요."),
	    
	    //405
	    INVALID_METHOD(HttpStatus.METHOD_NOT_ALLOWED ,"메소드 매칭이 되지 않습니다. 확인 후 다시 시도해주세요."),
	    
	    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "데이터유효성 검사 결과, 규칙에 맞지 않는 데이터입니다." ),
	    
	    //500 (설정한 custom 예외 처리를 제외한 RuntimeException 을 포함한 모든 Exception Handler 처리)
	    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다. 서버팀에 문의해주세요."),
    ;
	
	
    private final HttpStatus httpStatus;
    
    private final String message;
	
	
}

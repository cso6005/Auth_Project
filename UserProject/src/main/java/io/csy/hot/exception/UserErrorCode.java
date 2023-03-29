package io.csy.hot.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
	
	// badRequest 로??
	// 따로 에러코드 번호 추가할지 말지 고민
	INVALID_SIGNUP_EMAIL_PARAMETER(HttpStatus.BAD_REQUEST,"회원가입 - 이메일 양식이 틀렸습니다."),

	EXISTED_SIGNUP_EMAIL_PARAMETER(HttpStatus.BAD_REQUEST, "회원가입 - 이미 존재하는 이메일입니다."),
    
	INVALID_LOGIN_PARAMETER(HttpStatus.BAD_REQUEST, "로그인 - 해당 계정은 존재하지 않습니다."),

	INVALID_TOKEN_TYPE(HttpStatus.BAD_REQUEST, "잘못된 토큰입니다. 확인해주세요."),
	
	NOT_FIND_ACCOUNT_BY_TOKEN(HttpStatus.BAD_REQUEST, "사용자 인증 객체 조회 불가. 계정을 찾을 수 없습니다.")
	;
	
    private final HttpStatus httpStatus;
    private final String message;
	
}

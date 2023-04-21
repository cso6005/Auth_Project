package io.csy.exception;

import java.util.Date;

import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {

	private String timestamp;

	private int status; // 에러 코드 번호

	private String error; // 에러명

	private String message;

	private String path;

	public static ResponseEntity<Object> toCommonErrorResponse(CommonErrorCode e, String requestURL) {

		return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.builder().timestamp(new Date().toString())
				.status(e.getHttpStatus().value()).error(e.name()).message(e.getMessage()).path(requestURL).build());
	}
	
	public static ResponseEntity<Object> toDataVaildErrorResponse(CommonErrorCode e, String detailMessage, String requestURL) {

		return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.builder().timestamp(new Date().toString())
				.status(e.getHttpStatus().value()).error(e.name()).message(e.getMessage() + " : " + detailMessage).path(requestURL).build());
	} 

	public static ResponseEntity<Object> toUserErrorResponse(UserErrorCode e, String requestURL) {

		return ResponseEntity.status(e.getHttpStatus()).body(ErrorResponse.builder().timestamp(new Date().toString())
				.status(e.getHttpStatus().value()).error(e.name()).message(e.getMessage()).path(requestURL).build());
	}

}

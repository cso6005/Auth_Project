package io.csy.web.exception;

public class CustomException extends RuntimeException {
	
    public CustomException(){
        super(ErrorCode.AUTHENTICATION_FAILED.getMessage());
    }

    public CustomException(Exception ex){
        super(ex);
    }

}

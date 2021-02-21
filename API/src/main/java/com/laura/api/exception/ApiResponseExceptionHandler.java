package com.laura.api.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.laura.api.payload.ExceptionMessage;

@ControllerAdvice
public class ApiResponseExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(value = {NullPointerException.class})
	public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request){
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		ExceptionMessage exceptionMessage = new ExceptionMessage(ex.getMessage(), status, ZonedDateTime.now(ZoneId.of("Z")));
		return new ResponseEntity<>(exceptionMessage, status);
	}
}

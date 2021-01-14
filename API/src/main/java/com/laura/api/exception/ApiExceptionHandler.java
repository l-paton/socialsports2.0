package com.laura.api.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import com.laura.api.payload.ExceptionMessage;

@ControllerAdvice
public class ApiExceptionHandler extends ExceptionHandlerExceptionResolver {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleControllerException(MethodArgumentTypeMismatchException ex, WebRequest req)
    {
    	HttpStatus badRequest = HttpStatus.BAD_REQUEST;
    	
    	ExceptionMessage exceptionMessage = new ExceptionMessage(
    			ex.getMessage(),
    			badRequest,
    			ZonedDateTime.now(ZoneId.of("Z")));
    	
        return new ResponseEntity<Object>(exceptionMessage, badRequest);
    }
}

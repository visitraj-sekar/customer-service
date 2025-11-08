package com.vc.customer.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomerGlobalExceptionHandler {
	
	@ExceptionHandler
	public ErrorResponse handleCustomerNotFoundException(CustomerNotFounException cne) {
		ErrorResponse err = null; //new ErrorResponse(HttpStatus.NOT_FOUND, cne.getMessage(), LocalDateTime.now());
		
		return err;
	}
	
	

}

package br.com.devdojo.controller.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.devdojo.controller.exception.ExceptionDetails;
import br.com.devdojo.controller.exception.ResourceNotFoundDetails;
import br.com.devdojo.controller.exception.ResourceNotFoundException;
import br.com.devdojo.controller.exception.ValidationExceptionDetails;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler  {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ResourceNotFoundDetails> handleResourceNotFoundException(ResourceNotFoundException exception){
		return new ResponseEntity<>(
				ResourceNotFoundDetails
				.builder()
				.timstamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.title("Resource not Found")
				.detail(exception.getMessage())
				.developerMessage(exception.getClass().getName())
				.build(), HttpStatus.NOT_FOUND
				);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
		String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
		
		return new ResponseEntity<>(
				ValidationExceptionDetails
				.builder()
				.timstamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.title("Field Validation Error")
				.detail("Check the field(s) below")
				.developerMessage(exception.getClass().getName())
				.fields(fields)
				.fieldMessage(fieldsMessage)
				.build(), HttpStatus.BAD_REQUEST);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
	 	ExceptionDetails exceptionDetails =  ExceptionDetails.builder()
		.timstamp(LocalDateTime.now())
		.status(status.value())
		.title(ex.getCause().getMessage())
		.detail(ex.getMessage())
		.developerMessage(ex.getClass().getName())
		.build();
		return new ResponseEntity<> (exceptionDetails, headers, status);
	}
}

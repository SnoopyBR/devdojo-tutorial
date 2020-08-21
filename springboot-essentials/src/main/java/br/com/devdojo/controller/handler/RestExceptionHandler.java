package br.com.devdojo.controller.handler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.devdojo.controller.exception.ResourceNotFoundDetails;
import br.com.devdojo.controller.exception.ResourceNotFoundException;
import br.com.devdojo.controller.exception.ValidationExceptionDetails;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

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
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationExceptionDetails> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
		
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
				.build(),
				HttpStatus.BAD_REQUEST);
	}
}

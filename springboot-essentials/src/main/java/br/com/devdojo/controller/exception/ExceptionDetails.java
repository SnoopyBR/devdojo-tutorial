package br.com.devdojo.controller.exception;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ExceptionDetails {
	
	protected String title;
	protected int status;
	protected String detail;
	protected LocalDateTime timstamp;
	protected String developerMessage;
	
}

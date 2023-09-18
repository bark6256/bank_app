package com.tencoding.bank.hendler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomPageException extends RuntimeException {
	
	private HttpStatus status;
	public CustomPageException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

}

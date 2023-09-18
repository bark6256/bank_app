package com.tencoding.bank.hendler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter // IoC 대상이 아니다 (필요 할 때 직접 new 할 예정)
public class CustomRestfullException extends RuntimeException {
	
	private HttpStatus status;
	public CustomRestfullException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}
}

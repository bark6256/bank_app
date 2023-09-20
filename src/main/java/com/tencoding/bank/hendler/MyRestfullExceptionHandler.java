package com.tencoding.bank.hendler;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tencoding.bank.hendler.exception.CustomRestfullException;
import com.tencoding.bank.hendler.exception.UnAuthorizedException;

/**
 * 예외 발생 시 (Json, XML)
 * 데이터를 가공해서 내려 줄 수 있다.
 */
@RestControllerAdvice
@Order(1)
public class MyRestfullExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public void exception(Exception e) {
		System.out.println("=============== restfull 예외 발생 =============");
		System.out.println(e.getMessage());
		System.out.println("================================================");
	}
	
	@ExceptionHandler(CustomRestfullException.class)
	public String basicException(Exception e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert('오류 발생 : " + e.getMessage() + "');");
		sb.append("history.back();");
		sb.append("</script>");
		return sb.toString();
	}
	
	@ExceptionHandler(UnAuthorizedException.class)
	public String notLoginException(UnAuthorizedException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert('" + e.getMessage() + "');");
		sb.append("history.back();");
		sb.append("</script>");
		return sb.toString();
	}
}

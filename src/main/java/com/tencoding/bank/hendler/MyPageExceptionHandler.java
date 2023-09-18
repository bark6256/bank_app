package com.tencoding.bank.hendler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tencoding.bank.hendler.exception.CustomPageException;

/**
 * view 랜더링을 위해 ModelAndView 기반으로
 * 객체를 반환하도록 설계할 때 사용
 * 예외 페이지를 return하도록 설계한다.
 */
@ControllerAdvice
public class MyPageExceptionHandler {

	@ExceptionHandler(Exception.class)
	public void exception(Exception e) {
		System.out.println("=============== 예외 발생 =============");
		System.out.println(e.getMessage());
		System.out.println("=======================================");
	}
	
	@ExceptionHandler(CustomPageException.class)
	public ModelAndView handleRuntimePageExcetion(CustomPageException e) {
		// ModelAndView 활용 방법
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
		modelAndView.addObject("message", e.getMessage());
		return modelAndView;
	}
}

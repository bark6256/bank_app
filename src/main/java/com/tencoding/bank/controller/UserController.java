package com.tencoding.bank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tencoding.bank.dto.SignInFormDto;
import com.tencoding.bank.dto.SignUpFormDto;
import com.tencoding.bank.hendler.exception.CustomRestfullException;
import com.tencoding.bank.repository.model.User;
import com.tencoding.bank.service.UserService;
import com.tencoding.bank.util.Define;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private HttpSession session;
	// 회원 가입 페이지 요청
	// http://localhost:80/user/sign-up
	@GetMapping("/sign-up")
	public String signUp() {
		return "user/signUp";
	}
	
	/**
	 * 회원 가입 처리
	 * @param dto
	 * @return 리다이랙트 : 로그인 페이지
	 */
	@PostMapping("/sign-up")
	public String signUpProc(SignUpFormDto dto) {
		System.out.println(dto.toString());
		// 유효성 검사
		if(dto.getUsername() == null
				|| dto.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력하세요"
					, HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null
				|| dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("password를 입력하세요"
					, HttpStatus.BAD_REQUEST);
		}
		if(dto.getFullname() == null
				|| dto.getFullname().isEmpty()) {
			throw new CustomRestfullException("fullname을 입력하세요"
					, HttpStatus.BAD_REQUEST);
		}
		userService.signUpService(dto);
		// 정상처리 완료시 - 리다이랙트
		return "redirect:/user/sign-in";
	}
	
	// 로그인 페이지 요청
	// http://localhost:80/user/sign-in
	@GetMapping("/sign-in")
	public String signIn() {
		return "user/signIn";
	}
	
	/**
	 * 로그인 기능 처리
	 * @param dto
	 * @return 
	 */
	@PostMapping("/sign-in")
	public String signInProc(SignInFormDto dto) {
		if(dto.getUsername() == null
				|| dto.getUsername().isEmpty()) {
			throw new CustomRestfullException("username을 입력하세요"
					, HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null
				|| dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("password를 입력하세요"
					, HttpStatus.BAD_REQUEST);
		}
		
		User principal = userService.signInService(dto);
		principal.setPassword(null);
		session.setAttribute(Define.PRINCIPAL, principal);
		
		return "redirect:/account/list";
	}
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/user/sign-in";
	}
}

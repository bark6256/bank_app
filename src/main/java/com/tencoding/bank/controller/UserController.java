package com.tencoding.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tencoding.bank.dto.SignUpFormDto;

@Controller
@RequestMapping("/user")
public class UserController {
	// 회원 가입 페이지 요청
	// http://localhost:80/user/sign-up
	@GetMapping("/sign-up")
	public String signUp() {
		return "user/signUp";
	}
	
	// 회원 가입 처리
	@PostMapping("/sign-up")
	public String signUpProc(SignUpFormDto dto) {
		System.out.println(dto.toString());
		// 1. 유효성 검사
		// 2. 이미지 처리
		// 3. 서비스 호출
		// 4. 정상처리 완료시 - 리다이랙트
		return "redirect:/user/sign-in";
	}
	
	// 로그인 페이지 요청
	// http://localhost:80/user/sign-in
	@GetMapping("/sign-in")
	public String signIn() {
		return "user/signIn";
	}
}

package com.tencoding.bank.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.tencoding.bank.dto.KakaoProfile;
import com.tencoding.bank.dto.OAuthToken;
import com.tencoding.bank.dto.SignInFormDto;
import com.tencoding.bank.dto.SignUpFormDto;
import com.tencoding.bank.hendler.exception.CustomRestfullException;
import com.tencoding.bank.repository.model.User;
import com.tencoding.bank.service.UserService;
import com.tencoding.bank.util.Define;

@Controller
@RequestMapping({"/user", "/"})
public class UserController {
	
	@Value("${tenco.key}")
	private String tencoKey;
	
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
	@GetMapping({"/sign-in", ""})
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
	
	@GetMapping("/kakao/callback")
	public String kakaoCallback(@RequestParam String code) {
		RestTemplate rt = new RestTemplate();
		// POST 방식 - exchange() 활용
		// header 구성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// body 구성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "891be07cd472112a139584ce6aec7832");
		params.add("redirect_uri", "http://localhost:80/user/kakao/callback");
		params.add("code", code);
		
		// header + body 결합
		HttpEntity<MultiValueMap<String, String>> reqMes 
			= new HttpEntity<>(params, headers);
		
		// http 요청 - 엑세스 토큰
		// dto 파싱
		ResponseEntity<OAuthToken> response 
			= rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST
						, reqMes, OAuthToken.class);
		
		// 사용자 정보 가져오기
		RestTemplate rt2 = new RestTemplate();
		// 헤더생성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + response.getBody().getAccessToken());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// 바디생성 - 바디가 없다. 생략
		// 헤더 바디 결합
		HttpEntity<MultiValueMap<String, String>> kakaoInfo 
			= new HttpEntity<>(headers2);
		ResponseEntity<KakaoProfile> response2 = rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoInfo, KakaoProfile.class);
		// Service 구성
		// - 최초 사용자일 경우 회원가입 처리
		// username - 동일한 값이 저장되지 않도록 처리를 해야한다
		// password - 직접 만들어서 넣어야 한다.
		// 소셜 로그인 사용자는 모든 패스워드가 동일하다.
		KakaoProfile kakaoProfile = response2.getBody();
		SignUpFormDto signUpFormDto = SignUpFormDto.builder()
											.username(kakaoProfile.getKakaoAccount().getEmail())
											.fullname(kakaoProfile.getKakaoAccount().getProfile().getNickname())
											.password(tencoKey)
											.build();
		
		// 2. 로그인 : 세션 메모리에 사용자 등록 (세션 생성)
		User oldUser = userService.searchUsername(signUpFormDto.getUsername());
		if(oldUser == null) {
			userService.signUpService(signUpFormDto);
			oldUser = User.builder()
						.username(signUpFormDto.getUsername())
						.fullname(signUpFormDto.getFullname())
						.build();
		} else {
			oldUser.setPassword(null);
		}
		session.setAttribute(Define.PRINCIPAL, oldUser);
		
		return "redirect:/account/list";
	}
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate();
		return "redirect:/user/sign-in";
	}
}

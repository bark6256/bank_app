package com.tencoding.bank.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tencoding.bank.dto.AccountSaveFormDto;
import com.tencoding.bank.dto.DepositFormDto;
import com.tencoding.bank.dto.WithdrawFormDto;
import com.tencoding.bank.hendler.exception.CustomRestfullException;
import com.tencoding.bank.hendler.exception.UnAuthorizedException;
import com.tencoding.bank.repository.model.Account;
import com.tencoding.bank.repository.model.User;
import com.tencoding.bank.service.AccountService;
import com.tencoding.bank.util.Define;

@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private HttpSession session;
	
	// 계좌 목록 페이지
	// http://localhost:80/account/list
	@GetMapping("/list")
	public String list(Model model) {
		// 1. 인증 여부 확인
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
		}
		
		List<Account> accountList = accountService.readAccountList(principal.getId());
		if(accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		} else {
			model.addAttribute("accountList", accountList);
		}
		
		return "account/list";
	}
	
	// 계좌 생성 페이지 이동
	// http://localhost:80/account/save
	@GetMapping("/save")
	public String save() {
		// 1. 인증 여부 확인
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
		}
		return "account/save";
	}
	
	/**
	 * 계좌 생성 기능 구현
	 * @param accountSaveFormDto
	 */
	@PostMapping("/save")
	public String saveProc(AccountSaveFormDto dto) {
		// 1. 인증 여부 확인
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
		}
		// 2. 유효성 검사
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new CustomRestfullException("계좌 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfullException("비밀 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		if(dto.getBalance() == null || dto.getBalance() < 0) {
			throw new CustomRestfullException("잘못된 입력입니다", HttpStatus.BAD_REQUEST);
		}
		
		accountService.saveService(dto, principal.getId());
		return "redirect:/account/list";
	}
	
	// 출금 페이지
	// http://localhost:80/account/withdraw
	@GetMapping("/withdraw")
	public String withdraw() {
		// 1. 인증 여부 확인
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
		}
		return "account/withdraw";
	}
	
	/**
	 * 출금 기능 구현
	 * @param dto
	 */
	@PostMapping("/withdraw")
	public String withdrawProc(WithdrawFormDto dto) {
		// 1. 인증 여부 확인
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
		}
		// 2. 유효성 검사
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력해야 합니다", HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount() <= 0) {
			throw new CustomRestfullException("0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		if(dto.getWAccountPassword() == null || dto.getWAccountPassword().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 비밀 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		
		accountService.updateAccountWithdarw(dto, principal.getId());
		
		return "redirect:/account/list";
	}
	
	// 입금 페이지
	// http://localhost:80/account/deposit
	@GetMapping("/deposit")
	public String deposit(Model model) {
		// 1. 인증 여부 확인
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
		}

		// 계좌 목록
		List<Account> accountList = accountService.readAccountList(principal.getId());
		if(accountList.isEmpty()) {
			model.addAttribute("accountList", null);
		} else {
			model.addAttribute("accountList", accountList);
		}
		
		return "account/deposit";
	}

	/**
	 * 입금 기능 구현
	 * @param dto
	 */
	@PostMapping("/deposit")
	public String depositProc(DepositFormDto dto) {
		// 1. 인증 여부 확인
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인이 필요합니다", HttpStatus.UNAUTHORIZED);
		}
		// 2. 유효성 검사
		if(dto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력해야 합니다", HttpStatus.BAD_REQUEST);
		}
		if(dto.getAmount() <= 0) {
			throw new CustomRestfullException("0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
		}
		if(dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("입금 계좌 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		
		accountService.updateAccountDeposit(dto);
		
		return "redirect:/account/list";
	}
	// 계좌이체 페이지
	// http://localhost:80/account/transfer
	@GetMapping("/transfer")
	public String transfer() {
		return "account/transfer";
	}
	
	
	
	// 상세 보기 페이지
	// http://localhost:80/account/detail
	@GetMapping("/detail")
	public String detail() {
		return "account/detail";
	}
}

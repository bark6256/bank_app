package com.tencoding.bank.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tencoding.bank.dto.AccountSaveFormDto;
import com.tencoding.bank.dto.DepositFormDto;
import com.tencoding.bank.dto.HistoryDto;
import com.tencoding.bank.dto.TransferFormDto;
import com.tencoding.bank.dto.WithdrawFormDto;
import com.tencoding.bank.hendler.exception.CustomRestfullException;
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
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
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
		return "account/save";
	}
	
	/**
	 * 계좌 생성 기능 구현
	 * @param accountSaveFormDto
	 */
	@PostMapping("/save")
	public String saveProc(AccountSaveFormDto accountSaveFormDto) {
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		// 유효성 검사
		if(accountSaveFormDto.getNumber() == null || accountSaveFormDto.getNumber().isEmpty()) {
			throw new CustomRestfullException("계좌 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		if(accountSaveFormDto.getPassword() == null || accountSaveFormDto.getPassword().isEmpty()) {
			throw new CustomRestfullException("비밀 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		if(accountSaveFormDto.getBalance() == null || accountSaveFormDto.getBalance() < 0) {
			throw new CustomRestfullException("잘못된 입력입니다", HttpStatus.BAD_REQUEST);
		}
		
		accountService.saveService(accountSaveFormDto, principal.getId());
		return "redirect:/account/list";
	}
	
	// 출금 페이지
	// http://localhost:80/account/withdraw
	@GetMapping("/withdraw")
	public String withdraw() {
		return "account/withdraw";
	}
	
	/**
	 * 출금 기능 구현
	 * @param withdrawFormDto
	 */
	@PostMapping("/withdraw")
	public String withdrawProc(WithdrawFormDto withdrawFormDto) {
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		// 유효성 검사
		if(withdrawFormDto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력해야 합니다", HttpStatus.BAD_REQUEST);
		}
		if(withdrawFormDto.getAmount() <= 0) {
			throw new CustomRestfullException("0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
		}
		if(withdrawFormDto.getWAccountNumber() == null || withdrawFormDto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		if(withdrawFormDto.getWAccountPassword() == null || withdrawFormDto.getWAccountPassword().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 비밀 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		
		accountService.updateAccountWithdarw(withdrawFormDto, principal.getId());
		
		return "redirect:/account/list";
	}
	
	// 입금 페이지
	// http://localhost:80/account/deposit
	@GetMapping("/deposit")
	public String deposit(Model model) {
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
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
	 * @param depositFormDto
	 */
	@PostMapping("/deposit")
	public String depositProc(DepositFormDto depositFormDto) {
		// 유효성 검사
		if(depositFormDto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력해야 합니다", HttpStatus.BAD_REQUEST);
		}
		if(depositFormDto.getAmount() <= 0) {
			throw new CustomRestfullException("0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
		}
		if(depositFormDto.getDAccountNumber() == null || depositFormDto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("입금 계좌 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		
		accountService.updateAccountDeposit(depositFormDto);
		
		return "redirect:/account/list";
	}
	// 계좌 이체 페이지
	// http://localhost:80/account/transfer
	@GetMapping("/transfer")
	public String transfer() {
		return "account/transfer";
	}
	
	/**
	 * 계좌 이체 기능
	 * @param transferFormDto
	 * @return 계좌 목록 페이지
	 */
	@PostMapping("/transfer")
	public String transferProc(TransferFormDto transferFormDto) {
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		// 유효성 검사
		if(transferFormDto.getAmount() == null) {
			throw new CustomRestfullException("금액을 입력해야 합니다", HttpStatus.BAD_REQUEST);
		}
		if(transferFormDto.getAmount() <= 0) {
			throw new CustomRestfullException("0원 이하일 수 없습니다", HttpStatus.BAD_REQUEST);
		}
		if(transferFormDto.getWAccountNumber() == null || transferFormDto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		if(transferFormDto.getWAccountPassword() == null || transferFormDto.getWAccountPassword().isEmpty()) {
			throw new CustomRestfullException("출금 계좌 비밀 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		if(transferFormDto.getDAccountNumber() == null || transferFormDto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfullException("이체 계좌 번호를 입력해 주세요", HttpStatus.BAD_REQUEST);
		}
		
		accountService.updateAccountTransfer(transferFormDto, principal.getId());
		
		return "redirect:/account/list";
	}
	
	// 상세 보기 페이지
	// http://localhost:80/account/detail/{accountId}?type=(all, deposit, withdraw)
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Integer id, @RequestParam(name = "type", defaultValue = "all", required = false) String type,
			Model model) {
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		// 계좌 정보 호출
		Account account = accountService.readAccount(id);
		// 계좌가 본인 계좌인지 확인
		if(account.getUserId() != principal.getId()) {
			throw new CustomRestfullException("자신의 계좌가 아닙니다", HttpStatus.BAD_REQUEST);
		}
		List<HistoryDto> historyList = accountService.readHistoryListByAccount(account.getUserId(), type);
		model.addAttribute("principal", principal);
		model.addAttribute("account", account);
		model.addAttribute("historyList", historyList);
		return "account/detail";
	}
}

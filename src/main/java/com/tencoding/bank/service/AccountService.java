package com.tencoding.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tencoding.bank.dto.AccountSaveFormDto;
import com.tencoding.bank.dto.DepositFormDto;
import com.tencoding.bank.dto.WithdrawFormDto;
import com.tencoding.bank.hendler.exception.CustomRestfullException;
import com.tencoding.bank.repository.interfaces.AccountRepository;
import com.tencoding.bank.repository.interfaces.HistoryRepository;
import com.tencoding.bank.repository.model.Account;
import com.tencoding.bank.repository.model.History;

@Service
public class AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private HistoryRepository historyRepository;
	
	/**
	 * 계좌 생성 Service
	 * @param dto
	 * @param principalId
	 */
	@Transactional
	public void saveService(AccountSaveFormDto dto, Integer principalId) {
		Account account = new Account();
		account.setNumber(dto.getNumber());
		account.setPassword(dto.getPassword());
		account.setBalance(dto.getBalance());
		account.setUserId(principalId);
		int resultRowCount = accountRepository.insert(account);
		System.out.println("오류다ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ");
		if(resultRowCount != 1) {
			throw new CustomRestfullException("계좌 생성 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 계좌 목록 보기 Service
	 * @param userId
	 * @return
	 */
	@Transactional
	public List<Account> readAccountList(Integer userId) {
		List<Account> results = accountRepository.findByUserId(userId);
		return results;
	}

	/**
	 * 계좌 출금 기능 Service
	 * @param dto
	 * @param id
	 */
	@Transactional
	public void updateAccountWithdarw(WithdrawFormDto dto, Integer id) {
		// 계좌 여부 확인
		Account accountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
		if(accountEntity == null) {
			throw new CustomRestfullException("해당 계좌가 없습니다", HttpStatus.BAD_REQUEST);
		}
		// 본인 계좌 확인
		if(accountEntity.getUserId() != id) {
			throw new CustomRestfullException("본인 소유의 계좌가 아닙니다", HttpStatus.BAD_REQUEST);
		}
		// 비밀번호
		if(!accountEntity.getPassword().equals(dto.getWAccountPassword())) {
			throw new CustomRestfullException("비밀번호가 틀렷습니다", HttpStatus.BAD_REQUEST);
		}
		// 입금 기능
		accountEntity.withdraw(dto.getAmount());
		accountRepository.updateById(accountEntity);
		// 거래 내역 입력 - History 객체 생성
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWAccountId(id);
		history.setWBalance(accountEntity.getBalance());
		history.setDAccountId(null);
		history.setDBalance(null);
		
		int result = historyRepository.insert(history);
		if(result != 1) {
			throw new CustomRestfullException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 계좌 입금 기능 Service
	 * @param dto
	 * @param id
	 */
	@Transactional
	public void updateAccountDeposit(DepositFormDto dto) {
		// 계좌 여부 확인
		Account accountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
		if(accountEntity == null) {
			throw new CustomRestfullException("해당 계좌가 없습니다", HttpStatus.BAD_REQUEST);
		}
		// 입금 기능
		accountEntity.deposit(dto.getAmount());
		accountRepository.updateById(accountEntity);
		// 거래 내역 입력 - History 객체 생성
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWAccountId(null);
		history.setWBalance(null);
		history.setDAccountId(accountEntity.getUserId());
		history.setDBalance(accountEntity.getBalance());
		
		int result = historyRepository.insert(history);
		if(result != 1) {
			throw new CustomRestfullException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}

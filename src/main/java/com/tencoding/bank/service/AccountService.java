package com.tencoding.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tencoding.bank.dto.AccountSaveFormDto;
import com.tencoding.bank.dto.DepositFormDto;
import com.tencoding.bank.dto.HistoryDto;
import com.tencoding.bank.dto.TransferFormDto;
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
	 * @param accountSaveFormDto
	 * @param principalId
	 */
	@Transactional
	public void saveService(AccountSaveFormDto accountSaveFormDto, Integer principalId) {
		Account account = new Account();
		account.setNumber(accountSaveFormDto.getNumber());
		account.setPassword(accountSaveFormDto.getPassword());
		account.setBalance(accountSaveFormDto.getBalance());
		account.setUserId(principalId);
		int resultRowCount = accountRepository.insert(account);
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
	 * @param withdrawFormDto
	 * @param id
	 */
	@Transactional
	public void updateAccountWithdarw(WithdrawFormDto withdrawFormDto, Integer id) {
		// 계좌 여부 확인
		Account accountEntity = accountRepository.findByNumber(withdrawFormDto.getWAccountNumber());
		if(accountEntity == null) {
			throw new CustomRestfullException("해당 계좌가 없습니다", HttpStatus.BAD_REQUEST);
		}
		// 본인 계좌 확인
		accountEntity.checkOwner(id);
		// 비밀번호
		accountEntity.checkPassword(withdrawFormDto.getWAccountPassword());
		// 잔액 확인
		accountEntity.checkBalance(withdrawFormDto.getAmount());
		// 입금 기능
		accountEntity.withdraw(withdrawFormDto.getAmount());
		accountRepository.updateById(accountEntity);
		// 거래 내역 입력 - History 객체 생성
		History history = new History();
		history.setAmount(withdrawFormDto.getAmount());
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
	 * @param depositFormDto
	 * @param id
	 */
	@Transactional
	public void updateAccountDeposit(DepositFormDto depositFormDto) {
		// 계좌 여부 확인
		Account accountEntity = accountRepository.findByNumber(depositFormDto.getDAccountNumber());
		if(accountEntity == null) {
			throw new CustomRestfullException("해당 계좌가 없습니다", HttpStatus.BAD_REQUEST);
		}
		// 입금 기능
		accountEntity.deposit(depositFormDto.getAmount());
		accountRepository.updateById(accountEntity);
		// 거래 내역 입력 - History 객체 생성
		History history = new History();
		history.setAmount(depositFormDto.getAmount());
		history.setWAccountId(null);
		history.setWBalance(null);
		history.setDAccountId(accountEntity.getUserId());
		history.setDBalance(accountEntity.getBalance());
		
		int result = historyRepository.insert(history);
		if(result != 1) {
			throw new CustomRestfullException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 계좌 이체 기능 Service
	 * @param transferFormDto
	 * @param id
	 */
	@Transactional
	public void updateAccountTransfer(TransferFormDto transferFormDto, Integer id) {

		// 계좌 여부 확인
		// 출금 계좌
		Account wAccountEntity = accountRepository.findByNumber(transferFormDto.getWAccountNumber());
		// 이체 계좌
		Account dAccountEntity = accountRepository.findByNumber(transferFormDto.getDAccountNumber());
		
		if(wAccountEntity == null) {
			throw new CustomRestfullException("출금 계좌 번호가 없는 번호입니다", HttpStatus.BAD_REQUEST);
		}
		if(dAccountEntity == null) {
			throw new CustomRestfullException("이체할 계좌 번호가 없는 번호입니다", HttpStatus.BAD_REQUEST);
		}
		// 본인 계좌 확인
		wAccountEntity.checkOwner(id);
		// 비밀번호 확인
		wAccountEntity.checkPassword(transferFormDto.getWAccountPassword());
		// 잔액 확인
		wAccountEntity.checkBalance(transferFormDto.getAmount());
		// 이체 기능
		wAccountEntity.withdraw(transferFormDto.getAmount());
		dAccountEntity.deposit(transferFormDto.getAmount());
		accountRepository.updateById(wAccountEntity);
		accountRepository.updateById(dAccountEntity);
		// 거래 내역 입력 - History 객체 생성
		History history = new History();
		history.setAmount(transferFormDto.getAmount());
		history.setWAccountId(wAccountEntity.getUserId());
		history.setWBalance(wAccountEntity.getBalance());
		history.setDAccountId(dAccountEntity.getUserId());
		history.setDBalance(dAccountEntity.getBalance());
		
		int result = historyRepository.insert(history);
		if(result != 1) {
			throw new CustomRestfullException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 단일 계좌 정보 조회
	 * @param id (계좌 pk)
	 * @return accountEntity
	 */
	public Account readAccount(Integer id) {
		Account accountEntity = accountRepository.findById(id);
		if(accountEntity == null) {
			throw new CustomRestfullException("해당 계좌를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		
		return accountEntity;
	}

	/**
	 * 단일 계좌에 대한 거래 내역 조회
	 * @param type = [all, deposit, withdraw]
	 * @param id
	 * @param type
	 * @return historyListEntity
	 */
	@Transactional
	public List<HistoryDto> readHistoryListByAccount(Integer id, String type) {
		List<HistoryDto> historyListEntity = historyRepository.findByHistoryType(id, type);
		
		return historyListEntity;
	}


}

package com.tencoding.bank.repository.model;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

import com.tencoding.bank.hendler.exception.CustomRestfullException;

import lombok.Data;

@Data
public class Account {
	private Integer id;
	private String number;
	private String password;
	private Long balance;
	private Integer userId;
	private Timestamp createdAt;
	
	// 출금 기능
	public void withdraw(Long amount) {
		this.balance -= amount;
	}
	// 입금 기능
	public void deposit(Long amount) {
		this.balance += amount;
	}
	// 계좌 소유자 확인
	public void checkOwner(Integer principalId) {
		if(this.userId != principalId) {
			throw new CustomRestfullException("계좌 소유자가 아닙니다", HttpStatus.FORBIDDEN);
		}
	}
	// 패스워드 체크
	public void checkPassword(String principalPassword) {
		if(!this.password.equals(principalPassword)) {
			throw new CustomRestfullException("계좌 비밀번호가 틀렷습니다", HttpStatus.BAD_REQUEST);
		}
	}
	// 잔액 여부 확인
	public void checkBalance(Long amount) {
		if(this.balance < amount) {
			throw new CustomRestfullException("계좌에 잔액이 부족합니다", HttpStatus.BAD_REQUEST);
		}
	}
}

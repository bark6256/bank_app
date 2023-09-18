package com.tencoding.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tencoding.bank.dto.SignUpFormDto;
import com.tencoding.bank.hendler.exception.CustomRestfullException;
import com.tencoding.bank.repository.interfaces.UserRepository;

@Service // IoC 대상 - 싱글톤 패턴 (객체가 하나만 생성된다)
public class UserService {
	// DAO - 데이터 베이스 연동
	// userRepository를 DI한다
	@Autowired
	private UserRepository userRepository;
	
	// 트랜잭션을 사용하는 이유 : 정상처리 - commit, 오류발생 - rollback
	@Transactional
	public void signUpService(SignUpFormDto dto) {
		int result = userRepository.insert(dto);
		if(result != 1) {
			throw new CustomRestfullException("회원가입 실패"
					, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

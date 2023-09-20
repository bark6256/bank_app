package com.tencoding.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tencoding.bank.dto.SignInFormDto;
import com.tencoding.bank.dto.SignUpFormDto;
import com.tencoding.bank.hendler.exception.CustomRestfullException;
import com.tencoding.bank.repository.interfaces.UserRepository;
import com.tencoding.bank.repository.model.User;

@Service // IoC 대상 - 싱글톤 패턴 (객체가 하나만 생성된다)
public class UserService {
	// DAO - 데이터 베이스 연동
	// userRepository를 DI한다
	@Autowired
	private UserRepository userRepository;
	
	// 트랜잭션을 사용하는 이유 : 정상처리 - commit, 오류발생 - rollback
	@Transactional
	public void signUpService(SignUpFormDto signUpFormDto) {
		int result = userRepository.insert(signUpFormDto);
		if(result != 1) {
			throw new CustomRestfullException("회원가입 실패"
					, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public User signInService(SignInFormDto signInFormDto) {
		User userEntity = userRepository.findByUsernameAndPassword(signInFormDto);
		if(userEntity == null) {
			throw new CustomRestfullException("아이디 또는 비밀번호가 틀렷습니다"
					, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return userEntity;
	}
}

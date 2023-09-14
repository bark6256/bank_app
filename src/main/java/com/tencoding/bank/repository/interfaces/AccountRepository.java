package com.tencoding.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tencoding.bank.repository.model.Account;

@Mapper
public interface AccountRepository {
	public int insert(Account account);
	public int updateById(Account account);
	public int deleteById(Integer id);
	
	public Account findById(Integer id);
	public List<Account> findAll();
	
	public List<Account> findByUserId(Integer principalid);
	
	public Account findByNumber(String number); // 계좌 번호로 검색
}

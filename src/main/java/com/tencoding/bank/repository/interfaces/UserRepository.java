package com.tencoding.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tencoding.bank.dto.SignUpFormDto;
import com.tencoding.bank.repository.model.User;

// ibatis -> 2.4버전 이후부터 MyBatis로 이름이 변경됨
@Mapper // Mapper 반드시 써워야 동작한다.
public interface UserRepository {
	
	public int insert(SignUpFormDto dto);
	public int updateById(User user);
	public int deleteById(Integer id);
	public User findById(Integer id);
	public List<User> findAll();
}

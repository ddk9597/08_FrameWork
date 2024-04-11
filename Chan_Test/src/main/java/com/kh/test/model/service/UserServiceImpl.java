package com.kh.test.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.test.model.dto.User;
import com.kh.test.model.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserMapper mapper;
	
	// @Autowired 
	// 1. 필드 이용
	// 2. setter 이용
	// 3. 생성자 이용
	// 의존성 주입(final 제거 후 autowired도 가능)

	@Override
	public List<User> selectUser(String searchMember) {
		
		return mapper.selectUser(searchMember);
	}
}

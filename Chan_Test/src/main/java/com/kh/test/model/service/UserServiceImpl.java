package com.kh.test.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.test.model.dto.User;
import com.kh.test.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper mapper;
	
	 
	@Override
	public int selectUser(String searchMember) {
		
		
		return mapper.searchMember(searchMember);
	}


	
}

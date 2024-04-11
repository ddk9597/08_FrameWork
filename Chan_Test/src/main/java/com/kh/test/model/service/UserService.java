package com.kh.test.model.service;

import java.util.List;

import com.kh.test.model.dto.User;

public interface UserService {

	/**
	 * @param searchMember
	 * @return
	 */
	List<User> selectUser(String searchMember);



}

package com.kh.test.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.test.model.dto.User;

@Mapper
public interface UserMapper {

	/**
	 * @param searchMember
	 * @return
	 */
	List<User> selectUser(String searchMember);


}

package com.kh.test.customer.model.service;

import java.util.List;

import com.kh.test.customer.model.dto.Customer;

public interface CustomerService {

	/** 고객 정보 추가하기
	 * @param name
	 * @param tel
	 * @param address
	 * @return
	 */
	int regist(String name, String tel, String address);

	
	

}

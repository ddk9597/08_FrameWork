package com.kh.test.customer.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.test.customer.model.dto.Customer;
import com.kh.test.customer.model.mapper.CustomerMapper;

@Service
public class CustomerServiceImpl implements CustomerService{

	@Autowired
	private CustomerMapper mapper;
	
	// 고객 정보 추가하기
	@Override
	public int regist(String name, String tel, String address) {
		
		Customer customer = new Customer();
		customer.setCustomerName(name);
		customer.setCustomerTel(tel);
		customer.setCustomerAddress(address);
		
		
		return mapper.regist(customer);
	}
}

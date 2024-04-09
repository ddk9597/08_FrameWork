package com.kh.test.customer.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.test.customer.model.dto.Customer;

@Mapper
public interface CustomerMapper {


	/**새 고객 추가하기
	 * @param name
	 * @param tel
	 * @param address
	 * @return
	 */
	int regist(Customer customer);

	
	

}

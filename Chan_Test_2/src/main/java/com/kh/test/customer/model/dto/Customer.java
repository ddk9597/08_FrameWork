package com.kh.test.customer.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter	// getter
@Setter // setter
@ToString //toString
@NoArgsConstructor // 기본생성자,
@AllArgsConstructor // 매개변수 생성자 자동완성
public class Customer {

		private int customerNo ;
		private String customerName;
		private String customerTel;
		private String customerAddress;
	
}

package com.kh.test.model.dto;

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
public class User {
	
	 private int userNo; 
	 private String userId;
	 private String userName;
	 private int userAge;
	
}

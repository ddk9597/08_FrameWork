package edu.kh.demo.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Lombok 라이브러리 이용
// Lombok : 자주 사용하는 코드를 컴파일 시 자동완성해주는 라이브러리
// -> DTO(기본생성자, getter/getter, toString), Log

@NoArgsConstructor 
// 전달인자가 없는 생성자 == 기본생성자
@Getter // Getter 자동완성
@Setter // Setter 자동완성
@ToString // toString 오버라이딩 자동 완성
public class MemberDTO {

	private String memberId;
	private String memberPw;
	private String memberName;
	private int memberAge;
	
	
	
}

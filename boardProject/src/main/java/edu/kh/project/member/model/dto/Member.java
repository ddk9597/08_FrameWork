package edu.kh.project.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// DTO(Data Transfer Object)
// 데이터 전달하는 객체
// - DB에 조회된 결과 또는 SQL 구문에 사용할 값을 전달하는 용도
// - 관련성 있는 데이터를 한 번에 묶어서 다룬다


@Getter		// Spring EL 구문에서 getter를 이용해서 값을 불러옴
			// + MyBatis 에서 dto 값을 전달할 때 
@Setter		// 커맨드 객체 
@ToString
@NoArgsConstructor	// 기본생성자 : 커맨드 객체 만들 때 필요
public class Member {
	
	   private int 		memberNo;		
	   private String 	memberEmail;
	   private String 	memberPw;
	   private String 	memberNickname;
	   private String 	memberTel;
	   private String 	memberAddress;
	   private String 	profileImg;
	   private String 	enrollDate;
	   private String 	memberDelFl;
	   private int 		authority; 
	
}

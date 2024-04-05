package edu.kh.project.myPage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @Builder : 빌더 패턴을 이용해 객체 생성 및 초기화를 쉽게 진행
// 단점 : 기본생성자가 생성이 안됨 -> myBatis 조회 결과를 담을 때 필요한 객체 생성 실패
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {

	private int fileNo;
	private String filePath;
	private String fileOriginalName;
	private String fileRename;
	private String fileUploadDate;  // toChar이용할 것임.
	private int memberNo;			// PK. Member 테이블과 join 가능
	
	private String memberNickname;


}

package com.kch.buildingSearch.common.util;

import java.text.SimpleDateFormat;

// 프로그램 전체적으로 사용 될 유용한 기능 모음
public class Utility {
	
	// 파일 이름 중복 방지 설정
	public static int seqNum = 1; // 1 ~ 99999까지 반복하게(파일 이름 중복 방지를 위해)
	
	// static으로 하는 이유 : 프로그램 시작하자 마자 읽어 static 메모리 영역에 저장,
	// 이후 불러오고자 할 때 더 편하게 불러올 수 있음
	// 클래스명.메서드명() 으로 메서드 불러오기 가능
			
	public static String fileRename(String originalFileName) {
		
		// 20240405100931_00001.jpg 식으로 파일 이름 저장
		
		// SimpleDateFormat : 시간을 원하는 형태의 문자열로 간단히 변경
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		// new java.util.Date() : 현재 시간을 저장한 자바 객체
		String date = sdf.format( new java.util.Date() ); // 현재 시간 기록 
		
		String number = String.format("%05d", seqNum);
		
		seqNum++; // 1 증가
		if(seqNum == 100000) seqNum = 1;
		
		// 확장자는 전달받은 originalFileName 에서 얻어오기
		// substring(인덱스) : 인덱스부터 끝까지 문자를 반환
		
		// "문자열".lastIndexOf(".") : 문자열에서 마지막 .의 인덱스 반환
		String ext = originalFileName.substring( originalFileName.lastIndexOf(".") );
		
		return date + "_" + number + ext;
		
	}
	
}

package com.kch.buildingSearch.main.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResult {
	
	private String sysRegno; // 시스템등록번호
    private String sggCd; // 시군구코드
    private String bjdongCd; // 법정동코드
    private String sggNm; // 자치구명
    private String bjdongNm; // 법정동명
    private String landGbn; // 지번구분
    private String bobn; // 본번
    private String bubn; // 부번
    private String address; // 주소
    private String raRegno; // 중개업등록번호
    private String rdealerNm; // 중개업자명
    private String cmpNm; // 사업자상호
    private String telno; // 전화번호
    private String stsGbn; // 상태구분
    private String beginDe; // 행정처분 시작일
    private String endDe; // 행정처분 종료일
    private int rnum; // 조회 개수
    private String roadCd; // 도로명코드
    private String bldgLoc; // 건물
    private String bldgBobn; // 건물 본번
    private String bldgBubn; // 건물 부번

    
}

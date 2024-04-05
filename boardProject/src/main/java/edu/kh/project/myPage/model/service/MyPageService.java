package edu.kh.project.myPage.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;

public interface MyPageService {

	/** 회원 정보 수정
	 * @param inputMember
	 * @param memberAddress
	 * @return result
	 */
	int updateInfo(Member inputMember, String[] memberAddress);

	/** 비밀번호 수정
	 * @param paramMap
	 * @param memberNo
	 * @return result
	 */
	int changePw(Map<String, Object> paramMap, int memberNo);

	/** 회원 탈ㅇ퇴
	 * @param memberPw
	 * @param memberNo
	 * @return
	 */
	int secession(String memberPw, int memberNo);

	/** 파일 업로드 테스트 1
	 * @param uploadFile : 올린 사진 파일
	 * @return path
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	String fileUpload1(MultipartFile uploadFile) throws IllegalStateException, IOException;

	/** 파일 업로드 테스트2 + DB
	 * @param memberNo
	 * @param uploadFile
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	int fileUpload2(int memberNo, MultipartFile uploadFile) throws IllegalStateException, IOException;

	/** 파일 목록 조회
	 * @return
	 */
	List<UploadFile> fileList();

	/** 파일 여러개 업로드
	 * @param aaaList
	 * @param bbbList
	 * @param loginMember
	 * @return
	 */
	int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo) throws IllegalStateException, IOException;

	/**프로필 이미지 변경하기
	 * @param profileImg
	 * @param loginMember
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	int profile(MultipartFile profileImg, Member loginMember) throws IllegalStateException, IOException;


	
	

	
	
	



}

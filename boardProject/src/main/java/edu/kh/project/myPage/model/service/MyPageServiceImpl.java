package edu.kh.project.myPage.model.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.common.util.Utility;
import edu.kh.project.member.model.dto.Member;
import edu.kh.project.myPage.model.dto.UploadFile;
import edu.kh.project.myPage.model.mapper.MyPageMapper;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class) // 모든 예외 발생 시 롤백
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{

	private final MyPageMapper mapper;
	
	// BCrypt 암호화 객체 의존성 주입
	private final BCryptPasswordEncoder bcrypt;
	
	// @RequiredArgsConstructor 를 이용했을 때 자동 완성 되는 구문
//	@Autowired
//	public MyPageServiceImpl(MyPageMapper mapper) {
//		this.mapper = mapper;
//	}
	
	// 회원 정보 수정
	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		
		// 입력된 주소가 있을 경우 
		// memberAddress를 A^^^B^^^C 형태로 가공
		
		// 주소 입력 X -> inputMember.getMemberAddress()  -> ",,"
		if( inputMember.getMemberAddress().equals(",,") ) {
			
			// 주소에 null 대입
			inputMember.setMemberAddress(null);
		
		} else { 
			//  memberAddress를 A^^^B^^^C 형태로 가공
			String address = String.join("^^^", memberAddress);
			
			// 주소에 가공된 데이터 대입
			inputMember.setMemberAddress(address);
		}
		
		// SQL 수행 후 결과 반환
		return mapper.updateInfo(inputMember);
	}
	
	// 비밀번호 수정
	@Override
	public int changePw(Map<String, Object> paramMap, int memberNo) {
		
		// 회원번호를 이용해 암호화된 비밀번호 조회하기
		String originPw = mapper.selectPw(memberNo);
		
		// 입력 받은 현재 비밀번호와 DB에서 조회한 새 비밀번호 비교
		
		// 다를 경우
		if( !bcrypt.matches((String)paramMap.get("currentPw"), originPw) ) {
			return 0;
		}
		// 같을 경우
		// 새 비밀번호 암호화 진행
		String encPw = bcrypt.encode( (String)paramMap.get("newPw") );
		
		// 새 비밀번호 변경하는 mapper 호출
		// 묶어서 보내는 이유 : Mapper에 전달 가능한 파라미터는 1개 뿐!!
		
		paramMap.put("encPw", encPw);
		paramMap.put("memberNo", memberNo);
		
		return mapper.changePw(paramMap);
		
		
	}
	
	@Override
	public int secession(String memberPw, int memberNo) {
		
		String originPw = mapper.selectPw(memberNo);
		
		if( !(bcrypt.matches(memberPw, originPw)) ) {
			return 0;
		}
		return mapper.secession(memberNo);
	}
	
	// 파일 업로드 테스트
	@Override
	public String fileUpload1(MultipartFile uploadFile) throws IllegalStateException, IOException {
		
		// MultipartFile이 제공하는 메서드
		// - getSize() : 파일 크기
		// - isEmpty() : 업로드한 파일이 없을 경우 true
		// - getOriginalFileName() : 원본 파일 명
		// - transferTo(경로) : 메모리 또는 임시 저장 경로에 업로드된 파일을 원하는 경로에 전송(서버 어떤 폴더에 저장할지 지정)
		
		// 파일 검사하기
		// 업로드한 파일이 없을 경우
		if( uploadFile.isEmpty() ) {
			return null;
		}
		
		// 업로드한 파일이 있을 경우
		// 지정한 파일 경로 + 파일명으로 서버에 저장
		// 예외처리 필요
		uploadFile.transferTo( new File("C:\\uploadFiles\\test\\"+uploadFile.getOriginalFilename() ) );
		
		// 웹에서 해당 파일에 접근할 수 있는 경로를 반환
		
		// 서버 : C:\\uploadFiles\\test\\a.jpg
		// 웹 접근 주소 : /myPage/file/a.jpg
		
		return "/myPage/file/" + uploadFile.getOriginalFilename();
	}
	
	
	// 파일 업로드 + DB 연결
	@Override
	public int fileUpload2(int memberNo, MultipartFile uploadFile) throws IllegalStateException, IOException {
		
		// 업로드 된 파일 있는지 검사하기
		
		if( uploadFile.isEmpty() ) { // 업로드한 파일이 없을 경우 == 선택된 파일이 없을 경우
			return 0;
		}
		
		/* DB에 파일 저장은 가능은 하나 부하를 줄이기 위해
		 * 서버에 파일을 따로 저장함
		 * 1) Db에는 서버에 저장할 파일 경로 저장
		 * 2) 서버에 파일을 따로 저장한 후 경로 반환 받기
		 * 3) 파일 저장 실패 시 강제 예외 발생(@Transactional)시, 롤백 진행
		 *  */
		
		// 1. 서버에 저장할 파일 경로 만들기
		// 파일이 저장될 폴더 경로
		String folderPath = "C:\\uploadFiles\\test\\";
		
		// 클라이언트가 파일이 저장된 폴더에 접근할 수 있는 주소
		String webPath = "/myPage/file/"; 
		
		// 2. Db에 전달 할 데이터를 DTO로 묶기
		// INSERT 호출하기

		// 변경된 파일명
		String fileRename = Utility.fileRename(uploadFile.getOriginalFilename() );
		
		// Builder  패턴을 이용해서 UploadFile 객체 생성
		// 장점 1) 반복되는 참조변수명, set 구문 생략
		// 장점 2) method chaining : 메서드 연결을 이용하여 한 줄로 작성 가능함
		// 
		UploadFile uf = UploadFile.builder().memberNo(memberNo)
											.filePath(webPath)
											.fileOriginalName(uploadFile.getOriginalFilename() )
											.fileRename(fileRename)
											.build();
											
		int result = mapper.insertUploadFile(uf);
		
		// 3. INSERT 성공 시 파일을 지정된 서버 폴더에 저장
		// INSERT 실패 시
		if(result == 0 ) return 0;
		
		// 삽입 성공 시
		// C:\\uploadFiles\\test\\변경된 파일명으로
		// 파일을 서버에 저장
		

		uploadFile.transferTo(new File(folderPath + fileRename));
		// -> ChekcedException 발생 -> 예외 처리 필수
		
		// @Transactional은 UnCheckedExcpetion만 처리
		//  -> rollbackFor 속성을 이용해서 롤백할 예외 범위를 수정
		
		return result;
	}
	
	// 파일 목록 조회
	@Override
	public List<UploadFile> fileList() {
		
		
		return mapper.fileList();
	}
	
	
	/** 파일 여러개 업로드
	 *
	 */
	@Override
	public int fileUpload3(List<MultipartFile> aaaList, List<MultipartFile> bbbList, int memberNo)
			throws IllegalStateException, IOException {
		
		
		int result1 = 0;
		
		// 1. aaaList 처리하기
		// 업로드된 파일이 없을 경우 확인
		
		for(MultipartFile file : aaaList) {
			if(file.isEmpty()) { // 파일 없으면 다음 파일
				
				continue;
			}
			
			// 파일 업로드2 메서드 호출
			// -> 파일 하나 업로드 + DB 삽입
			result1 += fileUpload2(memberNo ,file);
			
		}
		

		// 2. bbbList 처리
		
		int result2 = 0; 
		
		// 업로드된 파일이 없을 경우를 제외하고 업로드
		for(MultipartFile file : bbbList) {
			
			if(file.isEmpty()) { // 파일 없으면 다음 파일
				continue;
			}
			
			// fileUpload2() 메서드 호출
			// -> 파일 하나 업로드 + DB INSERT
			result2 += fileUpload2(memberNo, file);
		}
		
		
		return result1 + result2;
			
	}
	
	
}






package edu.kh.project.member.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.mapper.MemberMapper;

/* BCrypt 암호화
 * - 입력된 문자열(비밀번호)에 salt를 추가한 후 암호화
 * -> 암호화 할 때 마다 결과가 다름
 * 
 * ex) 1234 입력      -> 1adfw_e412tga!!
 *     다시 1234 입력 -> 1adfw_szfdbdfyqe!@# 
 *     매번 결과가 다르므로 해킹하기 어렵겠지?
 *     DB에서도 조회 불가하므로  Spring Security 이용해서 확인
 * 
 * ++++ 비밀번호 확인 방법 ++++
 * 
 * 	1. DB에서 조회 후 가져옴
 *  2. BCryptPasswordEncoder.matches(평문 비밀번호, 암호화된 비밀번호)
 *     -> 평문 비밀번호와 암호화된 비밀번호가 같은 경우 true
 *     -> 아니면 false 반환
 *     
 * ++ 로그인 / 비밀번호 변경 / 탈퇴 등 비밀번호가 입력되는 경우
 * 	  DB에 저장된 암호화된 비밀번호를 조회해서
 *    mathches() 메서드로 비교해야 한다!!
 * */

@Service // 비즈니스 로직 처리 역할 + Bean 등록(객체 자동생성 및 관리)
@Transactional // 해당 클래스 메서드 종료 시 까지 
		   	   // - 예외(RuntimeException)발생하지 않으면 commit,
			   // - 예외(RuntimeException)발생 시 rollback
			   // (AOP 기반 기술)
public class MemberServiceImpl implements MemberService{

	
	@Autowired // 등록된 Bean 중에서 같은 타입 또는 상속관계인 bean을 
			   // 자동으로 DI 해줌
	private MemberMapper mapper;
	
	// BCrypt 암호화 객체를 의존성 주입(SecurityConfig 참고)
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	
	// 로그인 서비스
	@Override
	public Member login(Member inputMember) {
		
		// 테스트(디버그 모드)
		
		// bcrypt.encode(문자열) : 문자열을 암호화하여 반환해줌
		 String bcryptPassword = bcrypt.encode(inputMember.getMemberPw());
		
		// 1. 이메일이 일치하면서 탈퇴하지 않은 회원 조회
		Member loginMember = mapper.login(inputMember.getMemberEmail());
		
		// 2. 만약에 일치하는 이메일이 없어서 조회 결과가 null인 경우
		if(loginMember == null) return null;
		
		// 3. 입력받은 비밀번호(inputMember.getMemberPw() ) (평문) 와
		// 	  암호화된 비밀번호(loginMember.getMemberPw() ) 
		//	  두 비밀번호가 일치하는지 확인하기
		
		// 일치하지 않으면 
		if( !bcrypt.matches(inputMember.getMemberPw(), loginMember.getMemberPw()) ) {
			return null;
		}
		
		// 로그인 결과에서 비밀번호 제거
		loginMember.setMemberPw(null);
		
		return loginMember; 
	}
	
	/** 회원가입 서비스
	 *
	 */
	@Override
	public int signup(Member inputMember, String[] memberAdderess) {
		
		// 주소가 입력되지 않으면
		// iuputMember.getMemberAddress() -> " , , "
		// memberAddress -> [,,]
		// 주소 입력 시 , 가 들어가야만 하는 경우가 있다...
		// 따라서 ,로 배열을 구분짓는 것은 불가하다.. 이에 데이터 가공이 필요함
		
		// 1. 주소가 입력된 경우
		if( !inputMember.getMemberAddress().equals(",,") ) {
			// String.join("구분자", 배열)
			// -> 배열의 모든 요소 사이에 "구분자"를 추가하여
			// 하나의 문자열로 만드는 메서드
			
			// 구분자로 "^^^" 쓴 이유 : 
			// -> 주소, 상세주소에 없는 특수문자 생성
			// -> 나중에 다시 3분할 할 때 구분자로 이용할 예정
			String address = String.join("^^^", memberAdderess);
			
			// inputMember 주소로 합쳐진 주소 세팅하기
			inputMember.setMemberAddress(address);
			
		} else { // 주소가 입력이 안된 경우(주소는 필수입력사항이 아니다)
			// db에 ",,"가 저장이 될건데.. null로 치환해서 저장.
			// -> notNUll 제약조건 없으므로 가능
			inputMember.setMemberAddress(null); // null로 저장
		}
		
		// 비밀번호를 암호화 하여 다시 inputMember에 setting
		String encPw = bcrypt.encode(inputMember.getMemberPw()); 
		inputMember.setMemberPw(encPw);
		
		// 트랜잭션 안해도 됨 : @Transactional
		
		// 회원가입 메서드 호출
		// -> Mybatis에 의해 자동으로 SQL 수행
		// (mapper 메서드 호출 시 SQL에 사용할 parameter는 하나만 전달 가능함)
		return mapper.signup(inputMember);
	}
	
	// 이메일 체크
	@Override
	public int checkEmail(String memberEmail) {
		return mapper.checkEmail(memberEmail);
	}
	
	
	// 닉네임 중복 체크
	@Override
	public int checkNickname(String memberNickname) {
		return mapper.checkNickname(memberNickname);
	}
	
	// 빠른 로그인
	// >일반 로그인에서 비밀번호 비교만 제외
	@Override
	public Member quickLogin(String memberEmail) {
		
		Member loginMember = mapper.login(memberEmail);
		
		// 탈퇴 또는 없는 경우
		if(loginMember == null) return null;
		
		// 조회된 비밀번호 null로 변경
		loginMember.setMemberPw(null);
		return loginMember;
	}
	
	@Override
	public List<Member> selectMemberList() {
		
		
		return mapper.selectMember();
	}
	
	
	
}

package edu.kh.project.member.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.member.model.dto.Member;

@Mapper // mapper에서 bean 등록 
		// mapper.xml 파일과 연동 필요함
public interface MemberMapper {

	/** 로그인 SQL 실행
	 * @param memberEmail
	 * @return loginMember
	 */
	public Member login(String memberEmail);

	/** 회원가입 SQL 실행
	 * @param inputMember
	 * @return int result(성공한 행 개수)
	 */
	public int signup(Member inputMember);

	/** 이메일 중복 체크
	 * @param memberEmail
	 * @return
	 */
	public int checkEmail(String memberEmail);

	/** 닉네임 중복 체크
	 * @param memberNickname
	 * @return
	 */
	public int checkNickname(String memberNickname);

	/** 회원 목록 조회
	 * @return
	 */
	public List<Member> selectMember();


}

package edu.kh.project.main.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MainMapper {

	/** 비밀번호 초기화
	 * @param map
	 * @return
	 */
	int resetPw(Map<String, Object> map);

	/** 전달받은 번호로 회원 탈퇴 복구
	 * @param resotreMemNo
	 * @return
	 */
	int restoreMem(int resotreMemNo);

}

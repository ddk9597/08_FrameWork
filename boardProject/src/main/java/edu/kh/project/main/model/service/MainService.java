package edu.kh.project.main.model.service;

public interface MainService {

	/** 비밀번호 초기화
	 * @param inputNo
	 * @return
	 */
	int resetPw(int inputNo);

	/** 탈퇴 회원 복구
	 * @param resotreMemNo
	 * @return
	 */
	int restoreMem(int resotreMemNo);

}

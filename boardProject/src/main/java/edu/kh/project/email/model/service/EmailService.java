package edu.kh.project.email.model.service;

import java.util.Map;

public interface EmailService {

	/** 이메일 보내기
	 * @param string
	 * @param email
	 * @return authKey(1또는 0)
	 */
	String sendEmail(String string, String email);

	/** 이메일, 인증번호 중복검사
	 * @param map 
	 * @return count
	 */
	int checkAuthKey(Map<String, Object> map);

}

package edu.kh.project.email.model.service;


public interface EmailService {

	/** 이메일 보내기
	 * @param string
	 * @param email
	 * @return authKey(1또는 0)
	 */
	String sendEmail(String string, String email);

}

package edu.kh.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** @Configuration
 * - 설정용 클래스임을 명시 
 *   + 객체로 생성해서 내부 코드를 서버 실행시 모두 수행
 *   
 * 
 */
@Configuration
public class SequrityConfig {

	@Bean // 수동생성 객체를 스프링에 등록(Bean 객체)
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}

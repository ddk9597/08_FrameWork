package edu.kh.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스프링 스케쥴러를 활성화 하는 어노테이션

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
// -> SecurityAutoConfiguration.class : 사이트 처음에 뜨는 로그인 화면(보안관련자동설정) 제외
public class BoardProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardProjectApplication.class, args);
	}

}

package edu.kh.todo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.zaxxer.hikari.HikariConfig;
/* @Configuration
 * - 설정용 클래스임을 명시
 *   + 객체로 생성해서 내부 코드를 서버 실행 시 모두 수행(시작하자마자 모두 수행)
 * 
 * @PropertySource("classpath:/config.properties")
 * - 지정된 경로의 properties 파일 내용을 읽어와 사용
 * - 사용할 properties 파일이 다수일 경우 해당 어노테이션을 연속해서 작성 가능
 * - src/main/resources 내부의 config.properties를 불러오겠다
 * 
 * @Bean
 * - 개발자가 수동으로 생성한 객체의 관리를 스프링에게 넘기는 어노테이션
 * (Bean 등록)
 * 
 * */


@Configuration
@PropertySource("classpath:/config.properties")
public class DBConfig {

	@Bean
	@configurationProperties(prefix="spring.data source.hikari")
	public HikariConfig hikariconfig() {
		// HikariCofig 설정 객체 생성
		// - HikariCP를 이용하기 위한 설정을 config.properties 에  작성했다.
		//   그것을 가져와 쓰기 위한 메서드
		return new HikariConfig();
	}
}

package com.kch.buildingSearch.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;

@Configuration // 실행 시 모든 메서드 읽어옴
@PropertySource("classpath:/config.properties") // 해당 주소의 파일에 작성된 내용을 여기 클래스에서 사용하겠다
public class FileConfig implements WebMvcConfigurer{

	// WebMvcConfigurer 상속 받아야 한다
	// 골라서 오버라이딩 가능(오버라이딩 강제 안함)
	
	// @Value : .beans.factory의 것을 import 한다
	
	
	// 파일 최대 크기
	@Value("${spring.servlet.multipart.file-size-threshold}")
	private long fileSizeThreshold;
	
	// 요청당 파일 최대 크기
	@Value("${spring.servlet.multipart.max-request-size}")
	private long maxRequestSize;
	
	// 개별 파일 최대 크기
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxFileSize;
	
	// 임계값 초과 시 파일 저장할 경로
	@Value("${spring.servlet.multipart.location}")
	private String location;
	
	// 프로필 이미지 요청 주소
	@Value("${my.profile.resource-handler}")
	private String profileResourceHandler;
	
	@Value("${my.profile.resource-location}")
	private String profileResourceLocation;

	//  게시글 이미지 요청 주소
	@Value("${my.board.resource-handler}")
	private String boardResourceHandler;  
	
	// 게시글 이미지 요청 시 연결할 서버 폴더 경로
	@Value("${my.board.resource-location}")
	private String boardResourceLocation; 
	
	
	
	
	// 자원 처리기 추가함
	// 요청 주소에 다라 서버 컴퓨터의 어떤 경로에 접근할 지 설정 가능
	// 외부 클라이언트의 요청과 내 컴퓨터를 연결하는 것
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		registry
		.addResourceHandler("/myPage/file/**") // 클라이언트 요청 주소 패턴 작성
		.addResourceLocations("file:///C:\\uploadFiles\\test\\"); 
		
		// 프로필 이미지 요청 - 서버 폴더 연결 추가
		// 언제든지 주소 요청 가능함
		registry
		.addResourceHandler(profileResourceHandler) // /myPage/profile
		.addResourceLocations(profileResourceLocation);
		
		// 게시글 이미지 요청 - 서버 폴더 연결 추가
		registry.addResourceHandler(boardResourceHandler)
		.addResourceLocations(boardResourceLocation);
		
		
		
	}
	
	/* MultipartResolver 설정*/
	@Bean
	public MultipartConfigElement configElement() {
		
		
		MultipartConfigFactory factory = new MultipartConfigFactory();
		
		// 바이트로 단위 지정
		factory.setFileSizeThreshold(DataSize.ofBytes(fileSizeThreshold));
		
		// 
		factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
		
		// 
		factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
		
		// 
		factory.setLocation(location);
		
		return factory.createMultipartConfig();
	}
	
	// MultipartResolver 객체를 bean으로 추가
	// -> 추가 후 위에서 만든 MultipartConfig를 자동으로 이용
	@Bean
	public MultipartResolver multipartResolver() {
		
		StandardServletMultipartResolver multipartResolver = new StandardServletMultipartResolver();
		
		return multipartResolver;
		
	}
	
	
}

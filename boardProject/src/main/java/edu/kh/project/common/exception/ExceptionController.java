package edu.kh.project.common.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;


/* Spring 예외 처리 방법
 * 
 * 1. 메서드에서 직접 처리(try-catch, throws)
 * 
 * 2. 컨트롤러 클래스에서 발생하는 예외를 모아서 처리
 * 	1) 컨트롤러 클래스에 예외 처리를 위한 메서드를 작성
 *  2) 해당 메서드 위에 @ExceptionHandler 어노테이션 추가
 * 
 * 3. 프로젝트 단위로 묶어서 발생하는 모든 예외 모아서 처리 가능
 * 	1) 별도 클래스 생성
 *  2) 클래스 위에 @ControllerAdvice 어노테이션 추가
 *  3) 클래스 내부에 @ExceptionHandler 가 추가된 메서드 작성
 * 
 * 
 * 
 * 
 * */



// @ControllerAdvice 어노테이션
// - ☆전역적 예외 처리☆
// - 리소스 바인딩
// - 메시지 컨버전

@ControllerAdvice
public class ExceptionController {
	
	// 404 발생 시 처리
	@ExceptionHandler(NoResourceFoundException.class)
	public String notFound() {
		return "error/404";
	}

	
	// 프로젝트에서 발생하는 모든 종류의 예외를 처리
	@ExceptionHandler(Exception.class)
	public String allExceptionHandler(Exception e, Model model) {
		
		e.printStackTrace();
		model.addAttribute("e", e);
		return "error/500";
	}
	
}


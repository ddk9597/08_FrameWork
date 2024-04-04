package edu.kh.project.common.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/* Filter : 요청, 응답 시 걸러내거나 추가할 수 있는 객체 
 * 
 * [필터 클래스 생성 방법]
 * 1. jakarta.servlet.Filter 인터페이스 상속(Tomcat 10 버전 이상에서 지원)
 * 2. doFilter() 메서드 오버라이딩
 * 
 * 
 * */

// 로그인이 되어있지 않은 경우 특정 페이지로 돌아가게 하기
public class LoginFilter implements Filter{

	// 필터 동작을 정의하는 메서드
	@Override
	public void doFilter(
		ServletRequest request, 
		ServletResponse response, 
		FilterChain chain)throws IOException, ServletException {
		
		// ServletRequest : HttpServletRequest의 부모타입
		// ServletResponse : HttpServletResponse의 부모타입
		// 사용하기 위해선 다운캐스팅 필요함
		
		// 다운캐스팅
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		// Session 얻어오기
		HttpSession session = req.getSession();
		
		// 세션에서 로그인한 회원 정보를 얻어왔으나 없을 때
		// == 로그인이 되어있지 않은 상태
		if( session.getAttribute("loginMember")==null ) {
			
			// response를 이용하여 원하는 곳으로 요청/응답을 날려버릴 수 있음
			// loginError 재요청
			resp.sendRedirect("/loginError");
			
		} else { // 로그인 되어있는 경우
			// FilterChain
			// 다음 필터 또는 DispatcherServlet과 연결된 객체로 연결
			
			// 다음 필터로 요청 응답 객체를 전달
			// 만약 없으면 DispatcherServlet으로 전달
			chain.doFilter(request, response);
			
			// -> config.filterConfig Class 만들기
		}
		
		
	}
	
}

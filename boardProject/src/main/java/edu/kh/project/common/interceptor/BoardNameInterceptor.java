package edu.kh.project.common.interceptor;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// interceptor : HandlerInterceptor 인터페이스를 상속 받아서 구현

@Slf4j
public class BoardNameInterceptor implements HandlerInterceptor{

	// 후처리 방식 : Controller -> dispatcherServlet 사이
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		// Controller 가 일을 다 하고 중간에 끼워넣는거야.. 
		// application scope에서 baoardTypeList 얻어오기
		ServletContext application = request.getServletContext();
		
		
		// boardTypeList : List안에 map 형태
		// [{boardCode:1, boardName=공지}, {boardCode:2, boardName=자유} ..]
		
		List<Map<String, Object>> boardTypeList =
		(List<Map<String, Object>>)application.getAttribute("boardTypeList");
		
		log.debug(boardTypeList.toString());
		
		// log.debug("boardCode : " + request.getAttribute("boardCode"));
		
		// Unniform Resource Identifier : 통합 자원 식별자
		// - 자원 이름(주소)만 봐도 무엇인지 구별할 수 있는 문자열 
		// ex) RequstMapping("URI")
		// uri : /board/1
		// url : location(ip주소)/board/1
		String uri = request.getRequestURI();
		
		log.debug("uri : " + uri);
		
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	
}

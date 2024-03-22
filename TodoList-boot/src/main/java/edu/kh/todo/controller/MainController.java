package edu.kh.todo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.service.TodoService;
import lombok.extern.slf4j.Slf4j;

@Slf4j // 로그 객체 자동 생성 : 로그 찍기 가능하게
@Controller// 요청, 응답 제어 역할 + Bean등록
public class MainController {
	
	@Autowired // 등록된 Bean 중 같은 타입 or 상속 관계를 DI(의존성 주입)
	private TodoService service;
	
	@RequestMapping("/") // get, post 가리지 않고 메인페이지 호출
	public String mainPage(Model model) {
		
		log.debug("service : " + service); //DI가 안된 경우 null 출력, 된 경우 주소 출력
		
		// Service 메서드 호출 후결과 반환 받기
		Map<String, Object> map = service.selectAll();
		
		// Map에 담긴 포장 추출하기
		List<Todo> todoList = (List<Todo>)map.get("todoList");
		int completeCount = (int)map.get("completeCount");
		
		// Model 을 이용해서 조회 결과를 request Scope에 추가 
		model.addAttribute("todoList", todoList);
		model.addAttribute("completeCount", completeCount);
		
		// classpath:/templates/common/main.html로 forward
		return "common/main";
	}
	
}

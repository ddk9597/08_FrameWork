package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // bean 등록
public class MainController {
	
	@RequestMapping("/") // "/" 요청 매핑, 모든 메서드 요청 받아내기(get post 구분 x)
	public String mainPage() {
		
		
		return "common/main";
		
	}
	
	
	
}

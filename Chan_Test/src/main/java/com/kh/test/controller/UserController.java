package com.kh.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.test.model.dto.User;
import com.kh.test.model.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
	

	@Autowired
	private UserService service;
	
	
	@GetMapping("select")
	private String selectUser(
		@RequestParam("searchMember") String searchMember,
		RedirectAttributes ra
			) {
		
		int result = service.selectUser(searchMember);
		
		
		String path = null;
	    if (result > 0) {
	        path = "/searchSuccess";     
	        
	    } else if (result == 0) {
	        path = "/searchFail";       
	        
	    } else { // 검색 결과가 없는 경우
	        result = -1; // 특정한 값을 반환하도록 설정
	        path = "/searchFail"; // 실패 페이지로 이동
	    }
		
		
		return path;
	}
	
	
	

	
	 
	
}

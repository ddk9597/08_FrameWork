package com.kh.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.test.model.dto.User;
import com.kh.test.model.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService service;
	
	@GetMapping("select")
private String selectUser(
	@RequestParam("searchMember") String searchMember,
	Model model
		) {
	
		List<User> userList = service.selectUser(searchMember);
		
		String path = null;
		
	    if (userList.isEmpty()) {
	        path = "/searchFail";     
	        
	    } else { 
	        path = "/searchSuccess"; 
	        model.addAttribute("userList", userList);
		    }
			
			
			return path;
}
	
	
		
	
	

	
	 
	
}

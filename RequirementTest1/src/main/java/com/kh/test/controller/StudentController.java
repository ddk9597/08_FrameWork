package com.kh.test.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.test.model.dto.Student;
import com.kh.test.model.service.StudentService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentController {
	private final StudentService service;
	
//	// 메인페이지 설정
//	@RequestMapping("")
//	public String mainPage() {
//		return "static/index";
//	}
	
	@ResponseBody
	@GetMapping("/btn")
	public List<Student> rrr(
			Model model) {
		
		List<Student> stdList = new ArrayList<>();
		
		stdList = service.service();
		
		return stdList;
	}
	
	
	
}

package com.kch.book.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kch.book.dto.Book;
import com.kch.book.main.service.BookMainService;

import ch.qos.logback.core.model.Model;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("book")
public class BookController {

	@Autowired
	private BookMainService service;
	
	
	/** 비동기로 책 목록 조회
	 * @return
	 */
	@ResponseBody
	@GetMapping("selectBookList")
	public List<Book> selectBookList() {
		
		List<Book> selectBookList = service.selectBookList();
		
		return selectBookList;
		
	}
	
	/** 책 등록 화면으로 이동
	 * @return
	 */
	@GetMapping("registBook")
	public String registBook() {
		return "/registBook";
	}
	
	
	
	@PostMapping("doReg")
	public String doReg(
			@RequestParam("inputTitle") String inputTitle,
			@RequestParam("inputAuthor") String inputAuthor,
			@RequestParam("inputPrice") int inputPrice
			) {
		
		int result = service.doReg(inputTitle, inputAuthor, inputPrice);
		
		String message = null;
		String path = "redirect:/";
		
		
		return path;
	}
	
//	@ResponseBody
//	@GetMapping("editBookList")
//	public List<Book> editList
//	
	
	
	
	
	
	
	
	
	
	
	
	
}

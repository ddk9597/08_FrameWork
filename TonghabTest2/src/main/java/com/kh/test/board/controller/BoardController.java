package com.kh.test.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.test.board.model.dto.Board;
import com.kh.test.board.model.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("user")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService service;
	
	@GetMapping("select")
	private String selectUser(
		@RequestParam("searchMember") String searchMember,
		Model model
			) {
		
		List<Board> searchBoard = service.searchBoard(searchMember);
		
		model.addAttribute(searchBoard);
		
		String path = null;
		
		 if (searchBoard.isEmpty()) {
		        path = "/searchFail";     
		        
		    } else { 
		        path = "/searchSuccess"; 
		        model.addAttribute("searchBoard", searchBoard);
			    }
		
		return path;
	}
}


package edu.kh.project.board.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.kh.project.board.model.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("board")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService service;
	
	/** 게시글 목록 조회 : 게시판 종류 구분
	 * @param boardCode
	 * @param cp : 현재 조회 요청한 페이지
	 * @return
	 * 
	 * 
	 * 
	 * -/board/000
	 *  /board 이하 1레벨 자리에 숫자로된 요청주소가 
	 *  작성되어 있을 때에만 동작하겠다
	 *  (게시판 종류가 있을때에만 동작)
	 *  -- 정규표현식을 이용함.
	 *  0-9 : 한 칸에 0~9 숫자 입력 가능
	 *    + : 한 칸 이상(어느 칸에든 숫자 입력 가능하게)
	 * 
	 * 
	 */
	@GetMapping("{boardCode:[0-9]+}")
	public String selectBoardList(
			@PathVariable("boardCode") int boardCode,
			@RequestParam(value="cp", required = false, defaultValue = "1") int cp,
			Model model
		) {
		
		// cp : 현재 페이지, 필수아님, 기본값 1 int 자료형
		
		log.debug("boardCode :" + boardCode);
		
		// 조회 서비스 호출 후 결과 반환
		Map<String, Object> map = service.selectBoardList(boardCode, cp);
		
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
		return "board/boardList"; // boardList.html로 forward
	}
	
}

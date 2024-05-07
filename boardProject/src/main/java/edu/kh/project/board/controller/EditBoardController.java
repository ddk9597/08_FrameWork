package edu.kh.project.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.service.BoardService;
import edu.kh.project.board.model.service.EditBoardService;
import edu.kh.project.member.model.dto.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("editBoard")
@RequiredArgsConstructor
@Slf4j
public class EditBoardController {

	private final EditBoardService service;
	
	private final BoardService boardService;
	
	
	@GetMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(
		@PathVariable("boardCode") int boardCode) {
		
		return "board/boardWrite";
	}
	
	/** 게시글 작성
	 * @param boardCode : 작성 게시판 구분 용도
	 * @param inputBoard : 입력된 값(제목, 내용) + 추가 데이터 저장(커맨드 객체)
	 * @param loginMember : 로그인한 회원 번호 얻어오기
	 * @param images : 제출된 file 타입 input 태그 데이터들(사진)
	 * @param ra : 작성 완료 후 리다이렉트(requestScope로 데이터 전달)
	 * @return
	 */
	@PostMapping("{boardCode:[0-9]+}/insert")
	public String boardInsert(
		@PathVariable("boardCode") int boardCode,
		@ModelAttribute Board inputBoard,
		@SessionAttribute("loginMember") Member loginMember,
		@RequestParam("images") List<MultipartFile> images,
		RedirectAttributes ra) throws IllegalStateException, IOException 
		{
		
		/* **** 전달되는 파라미터 확인 ****
		 * 
		 * List<MultipartFile>
		 *  - 5개 모두 업로드      => 0~4번 인덱스에 파일 저장됨
		 *  - 5개 모두 업로드 안함 => 0~4번 인덱스에 파일 저장 안됨
		 *  - 2번 인덱스만 업로드  => 
		 *  	올린 해당 인덱스만 파일 저장, 
		 *  	나머지 인덱스에는 저장 안됨
		 * 
		 * [문제점]
		 * 	파일이 선택 되지 않은 input 태그도 제출되고 있음.
		 *  (제출은 되었지만 데이터는 빈 칸(""))
		 *  
		 *  -> 파일 선택이 안된 input 태그 값을 서버에 저장 하려고 하면
		 *     오류가 발생할 것이다.
		 * 
		 * [해결]
		 * - 무작정 서버에 저장하는 것이 아니라
		 * 	 제출된 파일이 있는지 확인하는 로직을 추가
		 * 
		 * 
		 * [추가]
		 *  List 요소의 index 번호 == IMG_ORDER와 같음
		 *  
		 * */
		
		// 1. boardCode, 로그인한 회원 번호를 inputBoard에 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		// 2. 서비스 메서드 호출 후 결과 반환 받기
		// -> 성공 시 [상세조회]를 요청할 수 있도록 삽입된 게시글 번호 반환 받기
		int boardNo = service.boardInsert(inputBoard, images);
		
		
		// 게시글 작성(INSERT)성공 시 -> 작성된 글 상세 조회로 redirect
		
		// 3. 서비스 결과에 따라 message, 리다이렉트 경로 지정
		
			String path = null;
			String message = null;
			
			if(boardNo > 0) {
				path = "/board/" + boardCode + "/" + boardNo; // 상세 조회
				message = "게시글이 작성 되었습니다";
				
			} else {
				
				path = "insert";
				message = "게시글 작성 실패";
			}
			
			ra.addFlashAttribute("message", message);
			

			// 게시글 작성(INSERT) 성공 시 -> 작성된 글 상세 조회로 redirect
			return "redirect:" + path;
		}
	
		
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/delete")
	public String delBulletin(
		@PathVariable("boardCode") int boardCode,
		@PathVariable("boardNo") int boardNo
		) {
		 
		Map<String, Object> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		int result = service.delBulletin(map);
		
		String path;
		
		if(result > 0) {
			path = "redirect:/board/" + boardCode;
		} else {
			path = "redirect:/board/" + boardCode + "/" + boardNo ;
			
		}
		
		
		return path;
	}
	
	/* 게시글 수정 화면 전환 */
	
	/**
	 * @param boardCode   : 게시판 종류
	 * @param boardNo     : 게시글 번호
	 * @param loginMember : 내가 쓴 글이 맞는지 검사용. 세션에서 가져옴
	 * 						getMapping으로 가져왔으니까
	 * @param model		  : 포워드 시 request scope로 값 전달하는 용도
	 * @param ra  		  : 리다이렉트 시 request scope로 값 전달하는 용도
	 * @return			  : 
	 */
	@GetMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(
		@PathVariable("boardCode") int boardCode,
		@PathVariable("boardNo")   int boardNo,
		@SessionAttribute("loginMember") Member loginMember,
		RedirectAttributes ra,
		Model model
		){
		
		// 수정 화면에 출력할 제목/내용/ 이미지 조회 == 게시글 상세 조회
		
		Map<String, Integer> map = new HashMap<>();
		map.put("boardCode", boardCode);
		map.put("boardNo", boardNo);
		
		// BoardService.selectOne(map)	호출
		// bean 등록 된 상태이므로 final 선언 하면 여기서 이용 가능함
		Board board = boardService.selectOne(map);
		
		String message = null;
		String path = null;
		
		if(board == null) {
			message = "해당 게시글이 존재하지 않습니다";
			path = "redirect/"; // 메인 페이지
			ra.addFlashAttribute("message", message);
		} else if(board.getMemberNo() != loginMember.getMemberNo() ) {
			message = "자신이 작성한 글만 수정 가능합니다";
			// 해당 글 상세 조회로 redirect
			path = String.format("redirect:/board/%d/%d", boardCode, boardNo);
			ra.addFlashAttribute("message", message);
		} else {
			path = "board/boardUpdate";
			model.addAttribute("board", board);
		}
		
		
		return path;
	}
	
	
	
	/** 게시글 수정
	 * 
	 * @param boardCode : 게시판 종류
	 * @param boardNo	: 게시글 번호
	 * @param loginMember : 로그인한 회원 번호 이용
	 * @param ra		
	 * @param inputBoard  : 커맨드객체(제목, 내용)
	 * @param images : 제출된 input type ="file" 요소
	 * @param deleteOrder : 삭제된 이미지 순서가 기록된 문자열(1,2,3, ...)
	 * @param querystring : 수정 성공 시 이전 파라미터 유지(cp, 검색어)
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@PostMapping("{boardCode:[0-9]+}/{boardNo:[0-9]+}/update")
	public String boardUpdate(
		@PathVariable("boardCode") int boardCode,
		@PathVariable("boardNo")   int boardNo,
		@ModelAttribute Board inputBoard,
		@SessionAttribute("loginMember") Member loginMember,
		@RequestParam("images") List<MultipartFile> images,
		RedirectAttributes ra,
		@RequestParam(value = "deleteOrder", required = false) String deleteOrder,
		@RequestParam(value = "querystring", required = false, defaultValue="") String querystring
		) throws IllegalStateException, IOException {
		
		// 1. 커맨드 객체 (inputBiard)에 boardCode, boardNo, memberNo 세팅
		inputBoard.setBoardCode(boardCode);
		inputBoard.setBoardNo(boardNo);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		// inputBoard -> 제목, 내용, boardCode, boardNo, memberNo
		
		// 2. 게시글 수정 서비스 호출 후 결과 반환 받기
		int result = service.boardUpdate(inputBoard, images, deleteOrder);
		
		// 3. 서비스 결과에 따라 응답 제어		 `
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "게시글이 수정 되었습니다";
			path = String.format("/board/%d/%d%s", boardCode, boardNo, querystring);
		} else {
			message = "수정 실패";
			path = "update"; // 상대경로 -> getMapping으로 바뀌어서 수정 화면으로 전환
		}
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
} // class
	
	


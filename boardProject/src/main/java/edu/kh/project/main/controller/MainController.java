package edu.kh.project.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.kh.project.main.model.service.MainService;
import lombok.RequiredArgsConstructor;

@Controller // bean 등록
@RequiredArgsConstructor
public class MainController {
	
	private final MainService service;
	
	@RequestMapping("/") // "/" 요청 매핑, 모든 메서드 요청 받아내기(get post 구분 x)
	public String mainPage() {
		
		
		return "common/main";
		
	}
	
	/** 비밀번호 초기화
	 * @param inputNo
	 * @return
	 */
	@ResponseBody
	@PutMapping("resetPw")
	public int resetPw(
		@RequestBody int inputNo) {
		
		return service.resetPw(inputNo);
	}

	
	/** 탈퇴 회원 복구
	 * @param resotreMemNo
	 * @return
	 */
	@ResponseBody
	@PutMapping("restore")
	public int restoreMem(
		@RequestBody int resotreMemNo) {
		
		return service.restoreMem(resotreMemNo);
	}
	
}

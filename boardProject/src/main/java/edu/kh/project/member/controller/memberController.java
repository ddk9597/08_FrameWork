package edu.kh.project.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.project.member.model.dto.Member;
import edu.kh.project.member.model.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;



@SessionAttributes({"loginMember"})
/* @SessionAttributes({"key", "key"})
 * 모델의 속성 중 특정 속성을 세션으로 올려주겠다.
 * 모델에 추가된 속성(attribute)중 
 * key값이 일치하는 속성을 session scope로 변경
 *  */
@Controller
@Slf4j // 로그
@RequestMapping("member")
public class memberController {
	
	@Autowired // DI(의존성 주입)
	private MemberService service;
	
	/* [로그인] 
	 * - 특정 사이트에 아이디, 비밀번호 입력
	 * - 해당 정보가 있으면 조회/서비스 이용 
	 * 
	 * - 로그인한 정보를 session에 기록하여 
	 * 	 로그아웃 또는 브라우저 종료 시 까지 
	 *   해당 정보를 계속해서 이용할 수 있게 함
	 *   
	 * */
	
	// 커맨드 객체
	// - 요청 시 전달받은 파라미터를 
	//   같은 이름의 필드에 세팅한 객체
	
	/**
	 * @param inputMember : 커맨드 객체(@ModelAttribute 생략)
	 * 						(필요한 변수들 세팅된 상태. 지금은 memberId, memberPw)
	 * @param ra : 리다이렉트시 1회성 데이터 전달.(request scope로 데이터 전달)
	 * 
	 * @param model : 데이터 전달용 객체(request scope)
	 * 
	 * @param savId : 아이디 저장 체크 여부
	 * @param resp : 쿠키 생성 추가를 위해 얻어온 객체
	 * 
	 * @return "redirect:/"
	 */
	@PostMapping("login")
	public String login(
		Member inputMember,
		RedirectAttributes ra,
		Model model,
		@RequestParam(value="saveId", required=false) String saveId,
		HttpServletResponse resp
		) {
		
		// 체크박스에 value가 없을 때
		// 체크가 된 경우   : "on" (null 아님)
		// 체크가 안된 경우 : null이 넘어옴 
		
		// log.debug("saveId : " + saveId);
		
		// 로그인 서비스 호출
		Member loginMember = service.login(inputMember);
		
		
		
		
		
		// 로그인 실패 시 
		if(loginMember == null) {
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다");
		}
		
		// 로그인 성공 시
		if(loginMember != null) {
			// Sessiong scope에 loginMember 추가
			
			model.addAttribute("loginMember", loginMember);
			// 1. request scope 에 저장됨
			
			// 2. 클래스 위에 @SessionAttributes() 어노테이션 때문에
			// 	  session scope로 이동된다
			
			/* ************************************************** */
			//아이디 저장(Cookie)
			
			// 쿠키 객체 생성 (K:V)
			Cookie cookie = new Cookie("saveId", loginMember.getMemberEmail());
			
			// 클라이언트가 어떤 요청을 할 때 쿠키가 첨부될지 지정
			
			// ex)"/" : IP 또는 도메인 또는 localhost 뒤에 "/" == 메인페이지 + 그 하위 주소들 
			cookie.setPath("/");
			
			// 만료 기간 지정
			if(saveId != null) { // 아이디 저장 체크 시
				cookie.setMaxAge(60*60*24*30); // 초 단위로 지정 30일
			} else { // 미체크 시
				cookie.setMaxAge(0); // 0초 지정 : 클라이언트 쿠키 삭제
			}
			
			// 응답 객체에 쿠키 추가 -> 클라이언트로 전달
			resp.addCookie(cookie);
			/* ************************************************** */
		}
		return "redirect:/"; // 멤버 페이지 재요청
	}
	
	/** 로그아웃 : Session에 저장된 로그인된 회원 정보를 없애기(만료, 무효화, 완료)
	 * @param SessionStatus : 세션을 완료(없앰)시키는 역할의 객체
	 *   					  - @SessionAttributes 로 등록된 세션을 완료
	 *      				  - 서버에서 기존 세션이 사라짐과 동시에 
	 *    				      - 새로운 세션 객체가 생성되어 클라이언트와 연결
	 * @return
	 */
	@GetMapping("logout")
	public String logout(SessionStatus status) {
		status.setComplete(); // 세션 완료 시킨(없앰)
		return "redirect:/";  // 메인 페이지 리다이렉트 
	}
		
	
	/** 로그인 페이지 이동
	 * @return
	 */
	@GetMapping("login")
	public String loginPage() {
		return "member/login";
	}
	
	/** 회원가입 페이지 이동 
	 * @return
	 */
	@GetMapping("signup")
	public String signUpPage() {
		
		return "member/signup";
				
	}
	
	
	/** 회원가입
	 * @return
	 * @param inputMember : 입력된 회원 정보
	 * 						(memberEmai, memberPw, memberTell, memberAddress(DTO❌, String Arr⭕)
	 * @param memberAddress : 입력한 주소 input 3개의 값을 배열로 전달
	 * @param ra : redirect시 request scope로 데이터를 전달하는 객체
	 */
	@PostMapping("signup")
	public String signup(
		/* @ModelAttribute */ Member inputMember,
		@RequestParam("memberAddress") String[] memberAdderess,
		RedirectAttributes ra
		) {
		
		
		
		return "redirect:/";
	}
	
		
}

//아이디 저장(Cookie)
		// cookie
		// - 클라이언트 측(브라우저) 에서 관리하는 데이터(파일 형식)
		// - 쿠키에는 만료기간, 데이터(key=value 문자열), 사용하는 사이트(주소)가 기록되어 있음
		// - 클라이언트가 쿠키에 기록된 사이트로 요청을 보낼 때
		//	 요청에 쿠키가 담겨져서 서버로 넘어감
		// - 쿠키의 생성, 수정, 삭제는 Server가 관리, 저장은 Client 가 함
		// - Cookie는 httpServletResponse를 이용해서 생성하고 Client 에게 전달(응답)할 수 있음

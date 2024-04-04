package edu.kh.project.main.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.kh.project.main.model.mapper.MainMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService{

	private final MainMapper mapper;
	
	private final BCryptPasswordEncoder bcrypt; // 암호화
	
	// 비밀번호 초기화
	@Override
	public int resetPw(int inputNo) {
		
		String pw = "pass01!";
		
		String encPw = bcrypt.encode(pw);
		
		Map<String, Object> map = new HashMap<>();
		map.put("encPw", encPw);
		map.put("inputNo", inputNo);
		
		// map 전달
		return mapper.resetPw(map);
	}
	
	// 탈퇴 회원 복구
	@Override
	public int restoreMem(int resotreMemNo) {
		
		// 번호를 이용해 복구하기
		return mapper.restoreMem(resotreMemNo);
	}
}

package edu.kh.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.mapper.TodoMapper;

@Service // 비즈니스로직(데이터가공, 트랜잭션 처리) 역할 명시
		 // + Bean으로 등록
public class TodoServiceImpl implements TodoService{
	
	@Autowired // 의존성 주입(DI)실행
	private TodoMapper mapper;
	
	// 할 일 목록 + 완료된 할 일 개수 조회
	@Override
	public Map<String, Object> selectAll() {
		
		// getConnection 할 필요 없음
		// DBConfig 에서 처리
		
		// 1. 할 일 목록 조회
		List<Todo> todoList = mapper.selectAll();
		
		// 2. 완료된 할 일 개수 조회 해서 결과 반환 받기
		int completeCount = mapper.getCompleteCount();
		
		// Map으로 묶어서 반환
		Map<String, Object> map = new HashMap<>();
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);
		
		return map;
		
		// 사용한 연결 객체 닫을 필요 없음
	}
	
}

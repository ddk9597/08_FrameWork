package edu.kh.todo.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.todo.model.dto.Todo;
import edu.kh.todo.model.mapper.TodoMapper;

//--------------------------------------------------
//@Transactional
//- 트랜잭션 처리를 수행하라고 지시하는 어노테이션
//(== 선언적 트랜잭션 처리)

//- 정상 코드 수행 시 COMMIT

//- 기본값 : Service 내부 코드 수행 중 RuntimeException 발생 시 rollback

//- rollbackFor 속성 : 어떤 예외가 발생했을 때 rollback할지 지정
//----------------------------

@Transactional(rollbackFor=Exception.class) // 모든 종류의 예외 발생 시 rollback 수행
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
	
	@Override
	public int addTodo(String todoTitle, String todoContent) {
		
		// Connection 생성/반환 x
		// 트랜잭션 제어 처리 -> @Transactional 어노테이션
		
		// 마이바티스에서 SQL에 전달할 수 있는 파라미터의 개수는
		// 딱 한개만 가능함..!!
		// 여러개인 경우 => 묶어서 보낸다
		// 지금의 경우 todoTitle, todoContent을 todo DTO 로 묶어서 전달
		Todo todo = new Todo();
		todo.setTodoTitle(todoTitle);
		todo.setTodoContent(todoContent);
		
		return mapper.addTodo(todo);
		
	}
	
}

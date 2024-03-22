package edu.kh.todo.model.service;

import java.util.Map;

public interface TodoService {
	
	// Spring에서 발생하는 예외는 대부분 Unchecked 상태(SQLException)
	// 따로 예외처리 하는 코드 쓸 일 거의 없음
	
	/** 할 일 목록 + 완료된 할 일 개수 조회
	 * @return map
	 */
	Map<String, Object> selectAll();

	/** 할 일 추가
	 * @param todoTitle
	 * @param todoContent
	 * @return result
	 */
	int addTodo(String todoTitle, String todoContent);
	
}

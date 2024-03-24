package edu.kh.todo.model.service;

import java.util.Map;

public interface TodoService {

	/** 할 일 목록 + 완료된 할 일 개수 조회
	 * @return map
	 * 
	 * throws SQL Exception 안해도 됨
	 * -> Spring 에서 발생하는 예외는 대부분 UnChecked 상태
	 */
	Map<String, Object> selectAll();

	/** 할 일 추가
	 * @param todoTitle
	 * @param todoContent
	 * @return result
	 */
	int addTodo(String todoTitle, String todoContent);

}

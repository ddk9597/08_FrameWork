package edu.kh.todo.model.service;

import java.util.List;
import java.util.Map;

import edu.kh.todo.model.dto.Todo;

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

	/** 할 일 상세 조회
	 * @param todoNo
	 * @return todo
	 */
	Todo todoDetail(int todoNo);

	/** 할 일 삭제하기
	 * @param todoNo
	 * @return result
	 */
	int deleteTodo(int todoNo);

	/** 할 일 수정
	 * @param todo
	 * @return result
	 */
	int todoUpdate(Todo todo);

	/** 할 일 완료 여부 수정
	 * @param todo 
	 * @return result
	 */
	int changeNY(Todo todo);

	/** ajax 전체 할 일 개수 조회 서비스
	 * @return totalCount
	 */
	int getTotalCount();

	/** 완료된 할 일 개수 조회
	 * @return
	 */
	int getCompleteCount();

	/** ajax 할 일 목록 조회
	 * @return todoList
	 */
	List<Todo> selectList();
	
}

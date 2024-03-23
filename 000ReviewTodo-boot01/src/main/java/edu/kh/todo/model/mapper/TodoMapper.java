package edu.kh.todo.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.todo.model.dto.Todo;

/* @Mapper
 * - Mybatis 제공 어노테이션
 * - 해당 어노테이션이 작성된 인터페이스는
 * 	 namespace에 해당 인터페이스가 작성된 
 *   mapper.xml 파일과 연결되어 SQL 호출/수행/결과 반환 가능
 *   
 * - 인터페이스 자체는 Bean으로 등록 못함
 *   상속은 가능함
 *   따라서 Mybatis에서 제공하는 Mapper 상속 객체가 Bean으로 자동 등록
 *   
 * */

@Mapper
public interface TodoMapper {
	
	/* Mapper의 메서드명 == mapper.xml 파일 내 태그의 id
	 * 메서드명과 id가 같은 태그가 서로 연결됨
	 * */
	
	/** 할 일 목록 조회 
	 * @return
	 */
	List<Todo> selectAll();

	/** 완료된 할 일 개수 조회
	 * @return completeCount
	 */
	int getCompleteCount();

	
	
}

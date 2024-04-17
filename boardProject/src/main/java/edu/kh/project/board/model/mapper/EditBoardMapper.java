package edu.kh.project.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	/** 게시글 작성(1단계)
	 * @param inputBoard
	 * @return result
	 */
	int boardInsert(Board inputBoard);

	/** 게시글 이미지 모두 삽입
	 * @param uploadList
	 * @return result 
	 */
	int insertUploadList(List<BoardImg> uploadList) ;

	/** 게시글 삭제하기
	 * @param boardCode
	 * @param boardNo
	 * @return
	 */
	int delBulletin(Map<String, Object> map);

	/** 게시글 부분(제목/내용) 수정
	 * @param inputBoard
	 * @return
	 */
	int boardUpdate(Board inputBoard);

	/** 게시글 수정 : 이미지 삭제
	 * @param map
	 * @return
	 */
	int deleteImage(Map<String, Object> map);

}

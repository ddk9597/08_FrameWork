package edu.kh.project.board.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.dto.Board;

public interface EditBoardService {

	/** 게시글 작성
	 * @param inputBoard
	 * @param images
	 * @return boardNo
	 */
	int boardInsert(Board inputBoard, List<MultipartFile> images) throws IllegalStateException, IOException;

	/** 게시글 삭제하기
	 * @param map 
	 * @param boardCode
	 * @param boardNo
	 * @return
	 */
	int delBulletin(Map<String, Object> map);

	/** 게시글 수정하기
	 * @param inputBoard
	 * @param images
	 * @param deleteOrder
	 * @return
	 */
	int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrder) throws IllegalStateException, IOException;

}

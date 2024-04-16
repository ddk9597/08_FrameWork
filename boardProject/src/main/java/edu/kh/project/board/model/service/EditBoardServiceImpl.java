package edu.kh.project.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.kh.project.board.model.BoardInsertException;
import edu.kh.project.board.model.dto.Board;
import edu.kh.project.board.model.dto.BoardImg;
import edu.kh.project.board.model.mapper.EditBoardMapper;
import edu.kh.project.common.util.Utility;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
@PropertySource("classpath:/config.properties")
public class EditBoardServiceImpl implements EditBoardService{

	private final EditBoardMapper mapper;
	
	// config.properties 값을 얻어와 필드에 저장
	@Value("${my.board.web-path}")
	private String webPath;
	
	@Value("${my.board.folder-path}")
	private String folderPath;
	
	
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) throws IllegalStateException, IOException {

		// 1. 게시글 부분(inputBoard)을 먼저 
		// BOARD 테이블에 INSET 하기
		// -> INSERT 결과로 작성된 게시글 번호(생성된 시퀀스 번호)반환 받기
		
		int result = mapper.boardInsert(inputBoard);
		// result == INSERT의 결과(0, 1)
		
		// mapper.xml에서 <selectKey> 태그를 이용해 생성된 boardNo가 inputBoard에 저장된 상태
		// (얕은 복사 개념 이해 필수)
		
		// 삽입 실패 시
		if(result == 0) return 0;
		
		// 삽입 성공 시 삽입된 게시글의 번호를 변수로 저장
		int boardNo = inputBoard.getBoardNo();
		
		
		// 2. 업로드된 이미지가 실제로 존재 할 경우 업로드된 이미지만 별도로 저장하여 
		// "BOARD_IMG" 테이블에 삽입하는 코드 작성
		
		// 실제 업로드 된 이미지의 정보를 모아둘 List 생성
		List<BoardImg> uploadList = new ArrayList<>();
		
		// images List에서 하나씩 꺼내서 선택 된 파일이 있는지 검사
		for( int i = 0 ; i < images.size() ; i ++) {
			
			// 실제 선택된 파일이 존재 하는 경우
			if( !images.get(i).isEmpty() ) {
				
				// IMG_PATH  == webPath
				// BOARD_NO  == boardNo
				// IMG_ORDER == i(인덱스 == 순서)
				
				// IMG_ORIGINAL_NAME
				String originalName = images.get(i).getOriginalFilename();
				
				// 변경명(유틸리티 이용)
				String rename = Utility.fileRename(originalName);
				
				BoardImg img = BoardImg.builder()
							   .imgOriginalName(originalName)
							   .imgRename(rename)
							   .imgPath(webPath)
							   .boardNo(boardNo)
							   .imgOrder(i)
							   .uploadFile(images.get(i))
							   .build();
				
				uploadList.add(img);
				
			}
			
		}
		
		// 실제 선택한 파일이 없을 경우(업로드를 1개도 안한 경우)
		// 여기서 종료함
		if(uploadList.isEmpty()) {
			return boardNo;
		}
		
		// 선택한 파일이 존재 할 경우
		// -> "BOARD_IMG" 테이블에 INSERT + 서버에 파일 저장
		
		/* 여러 행 삽입 방법 */
		// 1) 1행 삽입하는 SQL을 for문을 이용해서 여러 번 호출(간단한 방법)
		
		// 2) 여러 행을 삽입하는 SQL을 1회 호출 (이것을 사용할 것이다.)
		
		// result : 삽입된 행의 개수 == uploadList.size()
		result = mapper.insertUploadList(uploadList);
		
		// 다중 insert 성공 확인
		if(result == uploadList.size()) {
			
			// 서버에 파일 저장
			for(BoardImg img : uploadList) {
				
				// 메모리에 임시 저장된 파일 실제 폴더로 보내기
				img.getUploadFile().transferTo(new File(folderPath + img.getImgRename()  ));
				
			}
			
		} else {
			// 부분적으로 삽입 실패 한 경우 -> 전체 서비스 실패로 판단
			// -> 이전에 삽입된 내용 모두 rollback
			// -> rollback 하는 방법 == 강제로 RuntimeException 발생(@Transactional)
			
			throw new BoardInsertException("이미지 삽입 중 예외 발생");
		}
		
		return boardNo;
	}
	
}

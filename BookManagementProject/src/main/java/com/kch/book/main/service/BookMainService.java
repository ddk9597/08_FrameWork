package com.kch.book.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kch.book.dto.Book;

@Service
public interface BookMainService {


	/** 책 목록 조회
	 * @return
	 */
	List<Book> selectBookList();


	int doReg(String inputTitle, String inputAuthor, int inputPrice);


	/** 책 수정을 위해 비동기로 조회
	 * @param searchTitle
	 * @return
	 */
	List<Book> searchTitle(String searchTitle);

	
}

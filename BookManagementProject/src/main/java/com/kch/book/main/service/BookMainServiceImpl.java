package com.kch.book.main.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kch.book.dto.Book;
import com.kch.book.mapper.BookMapper;

@Service
public class BookMainServiceImpl implements BookMainService{

	@Autowired
	private BookMapper mapper;
	
	
	// 책 목록 비동기로 조회
	@Override
	public List<Book> selectBookList() {
		
		
		return mapper.selectBook();
	}
	
	
	@Override
	public int doReg(String inputTitle, String inputAuthor, int inputPrice) {
		
		Book book = new Book();
		book.setBookTitle(inputTitle);
		book.setAuthor(inputAuthor);
		book.setBookPrice(inputPrice);
		return mapper.doReg(book);
	}
	
}

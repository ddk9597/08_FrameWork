package com.kch.book.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kch.book.dto.Book;

@Mapper
public interface BookMapper {

	
	/** 책 목록 조회
	 * @return
	 */
	public List<Book> selectBook();

	
	/** 책 등록하기
	 * @return
	 */
	public int doReg(Book inputBook);

}

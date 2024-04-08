package com.kch.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

	private int bookNo;
	private String bookTitle;
	private String author;
	private int bookPrice;
	private String regDate; 
}

package com.example.demo.baechu.book.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.baechu.book.dto.BookListDto;
import com.example.demo.baechu.book.dto.FilterDto;
import com.example.demo.baechu.book.entity.Book;
import com.example.demo.baechu.book.repository.BookDSLRepository;
import com.example.demo.baechu.book.repository.BookRepository;
import com.example.demo.baechu.common.dto.BaseResponse;
import com.example.demo.baechu.common.exception.CustomException;
import com.example.demo.baechu.common.exception.ErrorCode;
import com.example.demo.baechu.common.exception.SuccessCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
	private final BookRepository bookRepository;
	private final BookDSLRepository bookDSLRepository;

	@Transactional(readOnly = true)
	public Map<String, Object> bookdetail(Long bookid) {
		Map<String, Object> info = new HashMap<>();
		Book book = bookRepository.findById(bookid).orElseThrow(
			() -> new IllegalArgumentException("해당 번호의 책 없음")
		);
		info.put("bookid", bookid);
		info.put("title", book.getTitle());
		info.put("image", book.getImage());
		info.put("price", book.getPrice());
		info.put("author", book.getAuthor());
		info.put("publish", book.getPublish());
		String birth = book.getYear() + "년 " + book.getMonth() + "월";
		info.put("birth", birth);
		info.put("inventory", book.getInventory());

		return info;
	}

	@Transactional(readOnly = true)
	public BookListDto searchByWord(FilterDto filter) {
		PageRequest pageRequest = PageRequest.of(filter.getPage(), filter.getTotalRow());
		List<Book> books = bookDSLRepository.searchBooks(filter, pageRequest);
		// Long totalCount = bookDSLRepository.getCount(filter);
		Long totalCount = 100L;
		return new BookListDto(filter.getPage(), totalCount, books);
	}

	@Transactional(readOnly = true)
	public List<Book> bookList() {

		List<Book> bookList = new ArrayList<>();
		Long random;
		Random r = new Random();

		for (int i = 0; i < 8; i++) {
			random = (long)r.nextInt(4000000);
			Optional<Book> book = bookRepository.findById(random);
			if (book.isPresent()) {
				bookList.add(book.get());
			} else
				i--;
		}

		return bookList;
	}

	@Transactional
	public ResponseEntity<BaseResponse> bookOrder(Long bookId, Long bookCall, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		if (session == null) {
			return BaseResponse.toResponseEntity(ErrorCode.Forbidden);
		} else {

			Book book = bookRepository.findById(bookId).orElseThrow(
				() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));

			Long inventory = book.getInventory();
			Long restOver = inventory - bookCall;
			if (restOver >= 0) {
				book.setInventory(restOver);
			} else {
				throw new CustomException(ErrorCode.INVALIDATION_ORDER);
			}
			return BaseResponse.toResponseEntity(SuccessCode.ORDER_SUCCESS);
		}
	}
}
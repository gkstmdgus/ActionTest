package com.example.demo.baechu.book.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.baechu.book.dto.BookListDto;
import com.example.demo.baechu.book.dto.FilterDto;
import com.example.demo.baechu.book.entity.Book;
import com.example.demo.baechu.book.service.BookService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookController {
	private final BookService bookService;

	@GetMapping("/detail/{id}")
	public String detailPage(Model model, @PathVariable Long id) {

		Map<String, Object> info = bookService.bookdetail(id);

		model.addAttribute("info", info);

		return "detail";
	}

	@GetMapping("/detail/buybooks/{bookId}/{quantity}")
	public String buyBook(@PathVariable Long bookId, @PathVariable Long quantity, HttpServletRequest request) {

		HttpStatus result = bookService.bookOrder(bookId, quantity, request).getStatusCode();

		if (result.isError()) {
			return "redirect:/login";
		} else {
			return "redirect:/main";
		}

	}

	@GetMapping("/search")
	public String searchByWord(
		Model model,
		@RequestParam(value = "query", defaultValue = "") String query,
		@RequestParam(value = "sort", defaultValue = "") Integer sort,
		@RequestParam(value = "year", defaultValue = "0") Integer year,
		@RequestParam(value = "star", defaultValue = "") Integer star,
		@RequestParam(value = "minPrice", defaultValue = "") Integer minPrice,
		@RequestParam(value = "maxPrice", defaultValue = "") Integer maxPrice,
		@RequestParam(value = "publish", defaultValue = "") String publish,
		@RequestParam(value = "author", defaultValue = "") String author,
		@RequestParam(value = "totalRow", defaultValue = "10") Integer totalRow,
		@RequestParam(value = "page", defaultValue = "0") Integer page,
		@RequestParam(value = "category", defaultValue = "") String category,
		@RequestParam(value = "babyCategory", defaultValue = "") String babyCategory
	) {
		FilterDto filter = createDto(query, sort, year, star, minPrice, maxPrice, publish, author, page, totalRow,
			category, babyCategory);
		BookListDto result = bookService.searchByWord(filter);
		model.addAttribute("result", result);
		return "search";
	}

	@GetMapping("/main")
	public String bookList(Model model) {

		List<Book> list = bookService.bookList();
		model.addAttribute("list", list);

		return "main";
	}

	private FilterDto createDto(String query, Integer sort, Integer year, Integer star, Integer minPrice,
		Integer maxPrice, String publish, String author, Integer page, Integer totalRow, String category,
		String babyCategory) {
		// 나중에 RequestParam을 따로 받지말고 하나의 객체로 받도록 수정 필요.
		return FilterDto.builder()
			.query(query)
			.sort(sort)
			.year(year)
			.star(star)
			.minPrice(minPrice)
			.maxPrice(maxPrice)
			.publish(publish)
			.author(author)
			.page(page)
			.totalRow(totalRow)
			.category(category)
			.babyCategory(babyCategory)
			.build();
	}
}
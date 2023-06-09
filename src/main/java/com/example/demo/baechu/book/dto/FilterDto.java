package com.example.demo.baechu.book.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FilterDto {
	private String query;
	private Integer sort;
	private Integer year;
	private Integer star;
	private Integer minPrice;
	private Integer maxPrice;
	private String publish;
	private String author;
	private Integer page;
	private Integer totalRow;
	private String category;
	private String babyCategory;

	@Builder
	public FilterDto(String query, Integer sort, Integer year, Integer star, Integer minPrice, Integer maxPrice,
		String publish, String author, Integer page, Integer totalRow, String category, String babyCategory) {
		this.query = query;
		this.sort = sort;
		this.year = year;
		this.star = star;
		this.minPrice = minPrice;
		this.maxPrice = maxPrice;
		this.publish = publish;
		this.author = author;
		this.page = page;
		this.totalRow = totalRow;
		this.category = category;
		this.babyCategory = babyCategory;
	}
}

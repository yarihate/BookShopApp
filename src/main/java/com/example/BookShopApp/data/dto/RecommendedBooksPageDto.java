package com.example.BookShopApp.data.dto;

import com.example.BookShopApp.data.model.book.BookEntity;

import java.util.List;

public class RecommendedBooksPageDto {
    private Integer count;
    private List<BookEntity> books;

    public RecommendedBooksPageDto(List<BookEntity> books) {
        this.books = books;
        this.count = books.size();
    }

    public List<BookEntity> getBooks() {
        return books;
    }

    public void setBooks(List<BookEntity> books) {
        this.books = books;
    }
}

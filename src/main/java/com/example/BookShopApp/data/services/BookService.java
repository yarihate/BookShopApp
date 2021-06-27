package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookEntity> getBooksData() {
        return bookRepository.findAll();
    }

    public List<BookEntity> getBooksByAuthorId(Integer authorId) {
        return null;
        //return bookRepository.findBooksByAuthor_Id(authorId);
    }
}

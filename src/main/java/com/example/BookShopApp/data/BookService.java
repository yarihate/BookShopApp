package com.example.BookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    public List<Book> getBooksData() {
//        List<Book> books = jdbcTemplate.query("SELECT * FROM books join authors on books.author_id = authors.id", (ResultSet rs, int row) -> {
//            Book book = new Book();
//            book.setId(rs.getInt("id"));
//            book.setAuthor(rs.getString("full_name"));
//            book.setTitle(rs.getString("title"));
//            book.setPrice(rs.getString("price"));
//            book.setPriceOld(rs.getString("priceOld"));
//            return book;
//        });
//        return new ArrayList<>(books);
//    }

    public List<Book> getBooksData() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int row) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author_id"));
            book.setTitle(rs.getString("title"));
            book.setPrice(rs.getString("price"));
            book.setPriceOld(rs.getString("priceOld"));
            return book;
        });
        return new ArrayList<>(books);
    }
}

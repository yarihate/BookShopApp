package com.example.BookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookService(NamedParameterJdbcTemplate jdbcTemplate) {
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
            book.setAuthor(getAuthorById(rs.getInt("author_id")));
            book.setTitle(rs.getString("title"));
            book.setPrice(rs.getString("price"));
            book.setPriceOld(rs.getString("price_old"));
            return book;
        });
        return new ArrayList<>(books);
    }

    private String getAuthorById(int author_id) {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors WHERE authors.id =" + author_id, (ResultSet rs, int rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));
            return author;
        });
        return authors.get(0).toString();
    }

    public List<Book> getBooksByAuthorId(Integer authorId) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("authorId", authorId);
        List<Book> books = jdbcTemplate.query("SELECT * FROM books where author_id = :authorId", parameterSource, (ResultSet rs, int row) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
//            book.setAuthor(rs.get("author_id"));
            book.setTitle(rs.getString("title"));
            book.setPrice(rs.getString("price"));
            book.setPriceOld(rs.getString("price_old"));
            return book;
        });
        return new ArrayList<>(books);
    }
}

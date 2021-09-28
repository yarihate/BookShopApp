package com.example.BookShopApp.controllers;

import com.example.BookShopApp.data.dto.BooksPageDto;
import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api("book data api")
public class BooksRestApiController {

    private final BookService bookService;

    @Autowired
    public BooksRestApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books/by-author")
    @ApiOperation("operation to get book list of bookshop by passed author first name")
    public ResponseEntity<List<BookEntity>> booksByAuthor(@RequestParam("author") String authorName) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorName));
    }

    @GetMapping("/books/by-title")
    @ApiOperation("get books by book title")
    public ResponseEntity<List<BookEntity>> booksByTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    @GetMapping("/books/by-price-range")
    @ApiOperation("get books by price range from min to max")
    public ResponseEntity<List<BookEntity>> priceRangeBooks(@RequestParam("min") Integer min, @RequestParam("max") Integer max) {
        return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
    }

    @GetMapping("/books/with-max-discount")
    @ApiOperation("get books with max discount")
    public ResponseEntity<List<BookEntity>> maxDiscountBooks() {
        return ResponseEntity.ok(bookService.getBooksWithMaxDiscount());
    }

    @GetMapping("/books/bestsellers")
    @ApiOperation("get bestsellers book (where is_bestseller = 1)")
    public ResponseEntity<List<BookEntity>> bestSellerBooks() {
        return ResponseEntity.ok(bookService.getBestsellers());
    }

    @GetMapping("/books/recent")
    @ApiOperation("get books by pub_date desc")
    public BooksPageDto booksByPubDate(@RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate from,
                                       @RequestParam(required = false) @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate to,
                                       @RequestParam("offset") Integer offset,
                                       @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getBooksByPubDate(from, to, offset, limit).getContent());
    }


    @GetMapping("/books/popular")
    @ApiOperation("get most popular books")
    public BooksPageDto popularBooks(@RequestParam("offset") Integer offset,
                                     @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPopularBooks(offset, limit).getContent());
    }

    @GetMapping("/books/tag/{id}")
    @ApiOperation("get books by tag")
    public BooksPageDto booksByTag(@PathVariable("id") Long id,
                                   @RequestParam("offset") Integer offset,
                                   @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getBooksByTag(id, offset, limit).getContent());
    }
}

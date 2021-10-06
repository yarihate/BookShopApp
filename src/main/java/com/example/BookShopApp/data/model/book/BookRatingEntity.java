package com.example.BookShopApp.data.model.book;

import com.example.BookShopApp.data.model.enums.BookRate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "book_rates")
public class BookRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    @Enumerated(EnumType.STRING)
    private BookRate bookRate;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public BookRate getBookRate() {
        return bookRate;
    }

    public void setBookRate(BookRate bookRate) {
        this.bookRate = bookRate;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}


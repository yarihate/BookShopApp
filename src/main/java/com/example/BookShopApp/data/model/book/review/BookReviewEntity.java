package com.example.BookShopApp.data.model.book.review;

import com.example.BookShopApp.data.model.book.BookEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_review")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(columnDefinition = "INT NOT NULL")
//    private int bookId;

    @Column(columnDefinition = "INT NOT NULL")
    private int userId;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;


    @OneToMany(mappedBy = "review")
    private List<BookReviewLikeEntity> likes = new ArrayList<>();

    public List<BookReviewLikeEntity> getLikes() {
        return likes;
    }

    public void setLikes(List<BookReviewLikeEntity> likes) {
        this.likes = likes;
    }

    public BookEntity getBook() {
        return book;
    }

    public void setBook(BookEntity book) {
        this.book = book;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(int bookId) {
//        this.bookId = bookId;
//    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String formattedDateTime() {
        if (time != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return time.format(formatter);
        } else {
            return "undefined";
        }
    }

    public Integer likeCount() {
        return (int) likes.stream().filter(reviewLike -> reviewLike.getValue() == 1).count();
    }

    public Integer disLikeCount() {
        return (int) likes.stream().filter(reviewLike -> reviewLike.getValue() == -1).count();
    }
}

package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.model.book.BookEntity;
import com.example.BookShopApp.data.model.book.review.BookReviewEntity;
import com.example.BookShopApp.data.model.book.review.BookReviewLikeEntity;
import com.example.BookShopApp.data.repositories.BookRepository;
import com.example.BookShopApp.data.repositories.ReviewLikeRepository;
import com.example.BookShopApp.data.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReviewService {
    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;


    @Autowired
    public ReviewService(BookRepository bookRepository, ReviewRepository reviewRepository, ReviewLikeRepository reviewLikeRepository) {
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.reviewLikeRepository = reviewLikeRepository;
    }


    public void addNewReview(String slug, String comment) {
        BookEntity book = bookRepository.findBookBySlug(slug);
        BookReviewEntity review = new BookReviewEntity();
        review.setText(comment);
        review.setTime(LocalDateTime.now());
        review.setBook(book);
        BookReviewEntity savedReview = reviewRepository.save(review);
        book.getReviews().add(savedReview);
        bookRepository.save(book);
    }


    public BookReviewEntity changeBookReviewRate(Integer reviewid, short value) {
        BookReviewEntity review = reviewRepository.findById(reviewid).get();
        BookReviewLikeEntity newReviewLike = new BookReviewLikeEntity();
        newReviewLike.setValue(value);

        newReviewLike.setTime(LocalDateTime.now());
        newReviewLike.setReview(review);
        BookReviewLikeEntity savedLike = reviewLikeRepository.save(newReviewLike);
        review.getLikes().add(savedLike);
        return reviewRepository.save(review);
    }
}

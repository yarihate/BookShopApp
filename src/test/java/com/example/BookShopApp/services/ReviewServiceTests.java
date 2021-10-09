package com.example.BookShopApp.services;

import com.example.BookShopApp.data.model.book.review.BookReviewEntity;
import com.example.BookShopApp.data.model.book.review.BookReviewLikeEntity;
import com.example.BookShopApp.data.repositories.ReviewLikeRepository;
import com.example.BookShopApp.data.repositories.ReviewRepository;
import com.example.BookShopApp.data.services.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

@SpringBootTest
class ReviewServiceImplTests {

    @MockBean
    private ReviewRepository reviewRepository;
    @MockBean
    private ReviewLikeRepository reviewLikeRepository;

    private final ReviewService reviewService;

    @Autowired
    ReviewServiceImplTests(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Test
    void changeBookReviewRate() {
        Integer reviewid = 1;
        short value = 2;

        BookReviewEntity review = new BookReviewEntity();
        review.setText("text");

        BookReviewLikeEntity newReviewLike = new BookReviewLikeEntity();
        newReviewLike.setReview(review);

        BookReviewEntity savedReview = new BookReviewEntity();
        savedReview.setLikes(Collections.singletonList(newReviewLike));

        Mockito.doReturn(Optional.of(review)).when(reviewRepository).findById(reviewid);
        Mockito.doReturn(newReviewLike).when(reviewLikeRepository).save(Mockito.any(BookReviewLikeEntity.class));
        Mockito.doReturn(savedReview).when(reviewRepository).save(Mockito.any(BookReviewEntity.class));

        BookReviewEntity changedReview = reviewService.changeBookReviewRate(reviewid, value);

        Mockito.verify(reviewRepository, Mockito.times(1)).findById(reviewid);
        Mockito.verify(reviewLikeRepository, Mockito.times(1)).save(Mockito.any(BookReviewLikeEntity.class));
        Mockito.verify(reviewRepository, Mockito.times(1)).save(Mockito.any(BookReviewEntity.class));

        Assertions.assertEquals(1, changedReview.getLikes().size());
        Assertions.assertTrue(changedReview.getLikes().contains(newReviewLike));
        Assertions.assertEquals("text", changedReview.getLikes().get(0).getReview().getText());
    }
}

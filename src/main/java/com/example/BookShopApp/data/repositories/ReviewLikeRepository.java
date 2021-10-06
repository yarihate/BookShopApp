package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.book.review.BookReviewLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<BookReviewLikeEntity, Integer> {
}


package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.book.review.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<BookReviewEntity, Integer> {
}

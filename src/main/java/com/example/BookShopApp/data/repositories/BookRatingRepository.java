package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.book.BookRatingEntity;
import com.example.BookShopApp.data.services.IBookRatingCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRatingRepository extends JpaRepository<BookRatingEntity, Integer> {

    @Query(value = "SELECT br.book_rate AS rate, COUNT(br.*) AS bookRateCount "
            + "FROM book_rates AS br WHERE br.book_id = ?1 GROUP BY br.book_rate ", nativeQuery = true)
    List<IBookRatingCount> countBookRatesCount(Integer id);

    long countByBookSlug(String slug);
}


package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.book.BookEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {
    //List<BookEntity> findBooksByAuthor_FirstName(String name);

    List<BookEntity> findBooksByAuthor_Id(Integer id);

    @Query("from BookEntity")
    List<BookEntity> customFindAllBooks();

    List<BookEntity> findBooksByAuthorNameContaining(String authorFirstName);

    List<BookEntity> findBooksByTitleContaining(String bookTitle);

    List<BookEntity> findBooksByPriceOldBetween(Integer min, Integer max);

    List<BookEntity> findBooksByPriceOldIs(Integer price);

    @Query("from BookEntity where isBestseller=1")
    List<BookEntity> getBestsellers();

    @Query(value = "SELECT * FROM books WHERE dicount = (SELECT MAX(discount) FROM books)", nativeQuery = true)
    List<BookEntity> getBooksWithMaxDiscount();

    Page<BookEntity> findBookByTitleContaining(String bookTitle, Pageable nextPage);

    @Query("FROM BookEntity b WHERE (CAST(:from AS date)  is null or b.pubDate > :from) and (CAST(:to AS date)is null or b.pubDate < :to) order by b.pubDate desc")
    Page<BookEntity> findBooksByPubDateIsBetweenOrderByPubDateDescIdAsc(@Param("from") LocalDate from, @Param("to") LocalDate to, Pageable nextPage);
}

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

    @Query(value = "select b.*,\n" +
            "       count(b2ut1.id) + (0.7 * count(b2ut2.id)) + (0.4 * count(b2ut3.id)) as popularity\n" +
            "from\n" +
            "    books b\n" +
            "        left join book2user b2u on b.id = b2u.book_id\n" +
            "        left join book2user_type b2ut1 on b2u.type_id = b2ut1.id and b2u.type_id = 3\n" +
            "        left join book2user_type b2ut2 on b2u.type_id = b2ut2.id and b2u.type_id = 2\n" +
            "        left join book2user_type b2ut3 on b2u.type_id = b2ut3.id and b2u.type_id = 1\n" +
            "group by b.id\n" +
            "order by popularity desc ", nativeQuery = true)
    Page<BookEntity> findPopularBooks(Pageable nextPage);
}

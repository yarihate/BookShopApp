package com.example.BookShopApp.data;

import com.example.BookShopApp.data.model.book.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<BookEntity, Integer> {
//    List<BookEntity> findBooksByAuthor_FirstName(String name);
//
//    List<BookEntity> findBooksByAuthor_Id(Integer id);
//
//    @Query("from BookEntity")
//    List<BookEntity> customFindAllBooks();
}

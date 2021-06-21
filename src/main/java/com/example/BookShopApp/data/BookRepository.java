package com.example.BookShopApp.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByAuthor_FirstName(String name);

    List<Book> findBooksByAuthor_Id(Integer id);

    @Query("from Book")
    List<Book> customFindAllBooks();
}

package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookstoreUserRepository extends JpaRepository<BookstoreUser, Integer> {
    BookstoreUser findBookstoreUserByEmail(String email);
}

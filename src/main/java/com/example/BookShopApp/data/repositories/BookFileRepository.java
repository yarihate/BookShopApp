package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.book.file.BookFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository <BookFileEntity, Integer> {
    BookFileEntity findBookFileByHash(String hash);
}

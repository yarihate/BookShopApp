package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.author.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Integer> {
}

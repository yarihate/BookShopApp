package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.UserDataEdition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserDataEditionRepository extends JpaRepository<UserDataEdition, UUID> {
}


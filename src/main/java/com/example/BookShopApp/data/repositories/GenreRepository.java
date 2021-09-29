package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.genre.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
    List<GenreEntity> findByParentIdIsNull();
    List<GenreEntity> findByParentIdIs(@Param("parentId") Integer parentId);
    GenreEntity findByNameIs(String genreName);
}

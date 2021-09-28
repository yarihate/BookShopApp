package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.tag.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Integer> {
    @Query(value = "select t.*, count(book2tag.id) as rate from tags t left join book2tag on t.id = book2tag.tag_id\n" +
            "group by t.id", nativeQuery = true)
    List<TagEntity> findAllTagsRate();
}

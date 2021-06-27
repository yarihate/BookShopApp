package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.TestEntity;
import org.springframework.data.repository.CrudRepository;

public interface TestEntityCrudRepository extends CrudRepository<TestEntity, Long> {
}

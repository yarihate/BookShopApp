package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.TestEntity;
import org.springframework.stereotype.Repository;

@Repository
public class TestEntityDao extends AbstractHibernateDao<TestEntity> {
    public TestEntityDao() {
        super();
        setClazz(TestEntity.class);
    }
}

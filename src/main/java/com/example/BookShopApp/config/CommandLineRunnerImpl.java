package com.example.BookShopApp.config;

import com.example.BookShopApp.data.repositories.BookRepository;
import com.example.BookShopApp.data.model.TestEntity;
import com.example.BookShopApp.data.repositories.TestEntityCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

// test Hibernate and Spring Data JPA
@Configuration
public class CommandLineRunnerImpl implements CommandLineRunner {
    //    EntityManagerFactory entityManagerFactory;
//    TestEntityDao testEntityDao;
    TestEntityCrudRepository testEntityCrudRepository;
    BookRepository bookRepository;

    @Autowired
    public CommandLineRunnerImpl(TestEntityCrudRepository testEntityCrudRepository, BookRepository bookRepository) {
        this.testEntityCrudRepository = testEntityCrudRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        for (int i = 0; i < 5; i++) {
//            createTestEntity(new TestEntity());
//        }
//
//        // TestEntity readEntity = testEntityDao.findOne(3L);
//        TestEntity readEntity = readTestEntityById(3L);
//        if (readEntity != null) {
//            Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info("read " + readEntity.toString());
//        } else {
//            throw new NullPointerException();
//        }
//
//        TestEntity updateEntity = updateTestEntityById(5L);
//        if (updateEntity != null) {
//            Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info("update " + updateEntity.toString());
//        } else {
//            throw new NullPointerException();
//        }
//
//        deleteTestEntityById(4L);

    //    Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info(bookRepository.findBooksByAuthor_FirstName("Giselle").toString());
    //    Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info(bookRepository.customFindAllBooks().toString());
    }

    private void deleteTestEntityById(Long id) {
        TestEntity testEntity = testEntityCrudRepository.findById(id).get();
        testEntityCrudRepository.delete(testEntity);
        //testEntityCrudRepository.deleteById(id);

//        Transaction tx = null;
//        try (Session session = entityManagerFactory.createEntityManager().unwrap(Session.class)) {
//            tx = session.beginTransaction();
//            TestEntity findEntity = readTestEntityById(id);
//            TestEntity mergedEntity = (TestEntity) session.merge(findEntity);
//            session.remove(mergedEntity);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            } else {
//                e.printStackTrace();
//            }
//        }
    }

    private TestEntity updateTestEntityById(Long id) {
        TestEntity testEntity = testEntityCrudRepository.findById(id).get();
        testEntity.setData("NEW DATA UPDATE");
        testEntityCrudRepository.save(testEntity);
        return testEntity;
//        Transaction tx = null;
//        TestEntity result = null;
//        try (Session session = entityManagerFactory.createEntityManager().unwrap(Session.class)) {
//            tx = session.beginTransaction();
//            TestEntity findEntity = readTestEntityById(id);
//            findEntity.setData("NEW DATA UPDATE");
//            result = (TestEntity) session.merge(findEntity);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            } else {
//                e.printStackTrace();
//            }
//        }
//        return result;
    }

    private TestEntity readTestEntityById(Long id) {
        return testEntityCrudRepository.findById(id).get();
//        Transaction tx = null;
//        TestEntity result = null;
//        try (Session session = entityManagerFactory.createEntityManager().unwrap(Session.class)) {
//            tx = session.beginTransaction();
//
//            result = session.find(TestEntity.class, id);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            } else {
//                e.printStackTrace();
//            }
//        }
//        return result;
    }

    private void createTestEntity(TestEntity testEntity) {
        testEntity.setData(testEntity.getClass().getSimpleName() + testEntity.hashCode());
        testEntityCrudRepository.save(testEntity);
//        Transaction tx = null;
//        try (Session session = entityManagerFactory.createEntityManager().unwrap(Session.class)) {
//            tx = session.beginTransaction();
//            testEntity.setData(testEntity.getClass().getSimpleName() + testEntity.hashCode());
//            session.save(testEntity);
//            tx.commit();
//        } catch (HibernateException e) {
//            if (tx != null) {
//                tx.rollback();
//            } else {
//                e.printStackTrace();
//            }
//        }
    }
}

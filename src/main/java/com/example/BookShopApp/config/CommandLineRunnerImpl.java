package com.example.BookShopApp.config;

import com.example.BookShopApp.data.TestEntity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.logging.Logger;

// test Hibernate
@Configuration
public class CommandLineRunnerImpl implements CommandLineRunner {
    EntityManagerFactory entityManagerFactory;

    @Autowired
    public CommandLineRunnerImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 5; i++) {
            createTestEntity(new TestEntity());
        }

        TestEntity readEntity = readTestEntityById(3L);
        if (readEntity != null) {
            Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info("read " + readEntity.toString());
        } else {
            throw new NullPointerException();
        }

        TestEntity updateEntity = updateTestEntityById(5L);
        if (updateEntity != null) {
            Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info("update " + updateEntity.toString());
        } else {
            throw new NullPointerException();
        }

        deleteTestEntityById(4L);
    }

    private void deleteTestEntityById(Long id) {
        Transaction tx = null;
        try (Session session = entityManagerFactory.createEntityManager().unwrap(Session.class)) {
            tx = session.beginTransaction();
            TestEntity findEntity = readTestEntityById(id);
            TestEntity mergedEntity = (TestEntity) session.merge(findEntity);
            session.remove(mergedEntity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            } else {
                e.printStackTrace();
            }
        }
    }

    private TestEntity updateTestEntityById(Long id) {
        Transaction tx = null;
        TestEntity result = null;
        try (Session session = entityManagerFactory.createEntityManager().unwrap(Session.class)) {
            tx = session.beginTransaction();
            TestEntity findEntity = readTestEntityById(id);
            findEntity.setData("NEW DATA UPDATE");
            result = (TestEntity) session.merge(findEntity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            } else {
                e.printStackTrace();
            }
        }
        return result;
    }

    private TestEntity readTestEntityById(Long id) {
        Transaction tx = null;
        TestEntity result = null;
        try (Session session = entityManagerFactory.createEntityManager().unwrap(Session.class)) {
            tx = session.beginTransaction();

            result = session.find(TestEntity.class, id);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            } else {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void createTestEntity(TestEntity testEntity) {
        Transaction tx = null;
        try (Session session = entityManagerFactory.createEntityManager().unwrap(Session.class)) {
            tx = session.beginTransaction();
            testEntity.setData(testEntity.getClass().getSimpleName() + testEntity.hashCode());
            session.save(testEntity);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            } else {
                e.printStackTrace();
            }
        }
    }
}

package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.payments.BalanceTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, Long> {
}

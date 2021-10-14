package com.example.BookShopApp.data.repositories;

import com.example.BookShopApp.data.model.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {

    public SmsCode findByCode(String code);
}

package com.example.BookShopApp.data.services;

import com.example.BookShopApp.data.model.SmsCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MessageSenderService {

    private final SmsService smsService;

    private final JavaMailSender mailSender;

    @Autowired
    public MessageSenderService(SmsService smsService, JavaMailSender mailSender) {
        this.smsService = smsService;
        this.mailSender = mailSender;
    }


    public void sendCodeViaEmail(String to, String subject) {
        SmsCode code = new SmsCode(smsService.generateCode(), 300);
        smsService.saveNewCode(code);
        sendMessageViaEmail(to, subject, "Verification code: " + code.getCode());
    }


    public void sendMessageViaEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bookstore00@mail.ru");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

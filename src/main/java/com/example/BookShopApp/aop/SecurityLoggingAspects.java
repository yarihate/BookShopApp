package com.example.BookShopApp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class SecurityLoggingAspects {
    Logger log = LoggerFactory.getLogger(SecurityLoggingAspects.class.getSimpleName());

    @AfterThrowing(value = "@annotation(com.example.BookShopApp.aop.annotations.AuthenticationExceptionTraceable)", throwing = "ex")
    public void logAuthenticationExceptionAdvise(AuthenticationException ex) {
        log.error("Authentication error. Message: {}", ex.getMessage());
    }


    @AfterThrowing(value = "execution(public * com.example.BookShopApp.data.services.BookstoreUserDetailsService.loadUserByUsername*(..))", throwing = "ex")
    public void logUsernameNotFoundExceptionExceptionAdvise(JoinPoint joinPoint, UsernameNotFoundException ex) {
        String username = (String) joinPoint.getArgs()[0];
        log.error("User: {} doesn't exit. Message: {}. Exception: {}", username, ex.getLocalizedMessage(), ex);
    }

    @AfterThrowing(value = "execution(public * com.example.BookShopApp.security.CustomAuthenticationProvider.authenticate(..))", throwing = "ex")
    public void logBadCredentialsExceptionExceptionAdvise(BadCredentialsException ex) {
        log.error("Authentications error: {}", ex.getLocalizedMessage());
    }
}

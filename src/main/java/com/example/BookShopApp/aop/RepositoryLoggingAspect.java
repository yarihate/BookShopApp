package com.example.BookShopApp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class RepositoryLoggingAspect {
    Logger log = LoggerFactory.getLogger(RepositoryLoggingAspect.class.getSimpleName());

    @Pointcut("execution(* save*(..))")
    public void save() {
        // Pointcut.
    }

    @Pointcut("execution(* delete*(..))")
    public void delete() {
        // Pointcut.
    }

    @Before(value = "within(org.springframework.data.jpa.repository.JpaRepository+) && (save() || delete())")
    public void beforeRepositoryCall(final JoinPoint joinPoint) {
        log.info(String.format("Repository call %s with arguments: %s.", joinPoint.toLongString(), Arrays.toString(joinPoint.getArgs())));
    }
}
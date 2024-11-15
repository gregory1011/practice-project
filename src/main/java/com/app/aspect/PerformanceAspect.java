package com.app.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
public class PerformanceAspect {

    @Pointcut("@annotation(com.app.annotation.ExecutionTime)")
    private void executionTimePC() {}

    @Around("executionTimePC()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeTime = System.currentTimeMillis();
        Object result = null;
        log.info("Method: {} - Execution starts:", joinPoint.getSignature());

        try {
            result = joinPoint.proceed();
        }catch (Throwable ex) {
            log.error("Error occurred during execution: {}", ex.getMessage());
            log.info("Execution time logger cannot proceed with class: {}", joinPoint.getSourceLocation().getFileName());
        }
        long afterTime = System.currentTimeMillis();
        log.info("Method: {} -Time taken to execute: {} ms", joinPoint.getSignature().toShortString(), (afterTime - beforeTime));
        return result;
    }

}

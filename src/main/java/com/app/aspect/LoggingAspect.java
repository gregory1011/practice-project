package com.app.aspect;

import com.app.dto.UserDto;
import com.app.service.CompanyService;
import com.app.service.SecurityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class LoggingAspect {

    private final SecurityService securityService;

//     catch if CompanyService activateCompany() or deactivateCompany() methods is executed
//     (..) method param are not important
    @Pointcut("execution(* com.app.service.CompanyService.activateCompany(..)) || execution(* com.app.service.CompanyService.deactivateCompany(..))")
    private void anyCompanyActivationDeactivation() {}

    @After(value = "anyCompanyActivationDeactivation()")
    private void afterCompanyActivationDeactivation(JoinPoint joinPoint) {
        UserDto loggedInUser = securityService.getLoggedInUser();
        log.info("Method: {}, Company Name: {}, User -> FirstName: {}, LastName: {}, Username: {}",
                joinPoint.getSignature().toShortString(),
                loggedInUser.getCompany().getTitle(),
                loggedInUser.getFirstname(),
                loggedInUser.getLastname(),
                loggedInUser.getUsername()
                );
    }

    @Pointcut("execution(* com.app..*(..))")
    private void anyRuntimeException() {}

    @AfterThrowing(pointcut = "anyRuntimeException()", throwing = "ex")
    private void afterRuntimeException(JoinPoint joinPoint, RuntimeException ex) {
        log.error("After Throwing -> Method: {} - Exception: {} - Message: {}",
                joinPoint.getSignature().toShortString(),
                ex.getClass().getSimpleName(),
                ex.getMessage()
        );
    }


}

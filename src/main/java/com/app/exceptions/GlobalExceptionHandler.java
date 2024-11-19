package com.app.exceptions;


import com.app.annotation.AccountingExceptionMessage;
import com.app.dto.common.DefaultExceptionMessageDto;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Optional;

@ControllerAdvice()
public class GlobalExceptionHandler {


    @ExceptionHandler(Throwable.class)
    public String handleThrowable(Throwable ex, HandlerMethod handlerMethod, Model model){
        ex.printStackTrace();
        Optional<DefaultExceptionMessageDto> defaultMessage= getMessageFromAnnotation(handlerMethod.getMethod());
        String displayMessage= "Something went wrong!";
        if(defaultMessage.isPresent()){
            displayMessage = defaultMessage.get().getMessage();
        } else if (ex.getMessage() != null) {
            displayMessage = ex.getMessage();
        }
        model.addAttribute("message", defaultMessage);
        return "error";
    }

    private Optional<DefaultExceptionMessageDto> getMessageFromAnnotation(Method method) {
        AccountingExceptionMessage defaultMessage = method.getAnnotation(AccountingExceptionMessage.class);
        if (defaultMessage != null) {
            DefaultExceptionMessageDto messageDto = DefaultExceptionMessageDto.builder()
                    .message(defaultMessage.defaultMessage())
                    .build();
            return Optional.of(messageDto);
        }else return Optional.empty();
    }

}

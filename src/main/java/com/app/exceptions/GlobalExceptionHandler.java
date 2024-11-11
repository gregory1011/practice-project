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
//        ex.printStackTrace();

        String message= "Something went wrong!";
        Optional<DefaultExceptionMessageDto> defaultMessage= getMessageFromAnnotation(handlerMethod.getMethod());
        if(defaultMessage.isPresent()){
            message = defaultMessage.get().getMessage();
        } else if (ex.getMessage() != null) {
            message = ex.getMessage();
        }
        model.addAttribute("message", message);
        return "/error";
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
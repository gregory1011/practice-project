package com.app.dto.common;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class DefaultExceptionMessageDto {

    private String message;
}

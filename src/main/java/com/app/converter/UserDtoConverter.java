package com.app.converter;

import com.app.dto.UserDto;
import com.app.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDtoConverter implements Converter<String, UserDto> {

    private UserService userService;

    @Override
    public UserDto convert(String source) {
        if (source.isEmpty()) return null;
        return userService.listById(Long.parseLong(source));
    }
}

package com.app.converter;

import com.app.dto.RoleDto;
import com.app.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RoleDtoConverter implements Converter<String, RoleDto> {

    private final RoleService roleService;

    @Override
    public RoleDto convert(String source) {
        if(source.isEmpty()) return null;
        return roleService.getRoleById(Long.parseLong(source));
    }
}

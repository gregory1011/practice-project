package com.app.converter;

import com.app.dto.RoleDto;
import com.app.service.RoleService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleDtoConvertor implements Converter<String, RoleDto> {

    private final RoleService roleService;

    public RoleDtoConvertor( RoleService roleService) {
        this.roleService = roleService;
    }


    @Override
    public RoleDto convert(String source) {
        if(source.isEmpty()) return null;
        return roleService.getRoleById(Long.parseLong(source));
    }
}

package com.app.service;

import com.app.dto.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto getRoleById(Long id);
    List<RoleDto> listAllRoles();
}

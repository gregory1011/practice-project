package com.app.service.impl;

import com.app.dto.RoleDto;
import com.app.entity.Role;
import com.app.repository.RoleRepository;
import com.app.service.RoleService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final MapperUtil mapperUtil;

    @Override
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(RuntimeException::new);
        return mapperUtil.convert(role, new RoleDto());
    }
}

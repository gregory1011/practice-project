package com.app.service.impl;

import com.app.dto.RoleDto;
import com.app.dto.UserDto;
import com.app.entity.Role;
import com.app.exceptions.RoleNotFoundException;
import com.app.repository.RoleRepository;
import com.app.service.RoleService;
import com.app.service.SecurityService;
import com.app.util.MapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    @Override
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(RoleNotFoundException::new);
        return mapperUtil.convert(role, new RoleDto());
    }

    @Override
    public List<RoleDto> listAllRoles() {
        UserDto loggedUser = securityService.getLoggedInUser();
        if(loggedUser.getRole().getDescription().equals("Root User")) {
            return roleRepository.findAll().stream()
                    .filter(role -> role.getDescription().equals("Admin")) // get all roles= Admin
                    .map(each -> mapperUtil.convert(each, new RoleDto())).toList();
        }else {
            return roleRepository.findAll().stream()
                    .filter(role -> !role.getDescription().equals("Root User")) // get all roles except Root User
                    .map(each -> mapperUtil.convert(each, new RoleDto()))
                    .toList();
        }
    }

}

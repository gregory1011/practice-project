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

import java.util.ArrayList;
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
        List<Role> list = roleRepository.findAll();
        UserDto loggedUser = securityService.getLoggedInUser();
        List<RoleDto> roles = new ArrayList<>();
        if(loggedUser.getRole().getDescription().equals("Root User")) {
            roles= list.stream().filter(each -> each.getDescription().equals("Admin")).map(each -> mapperUtil.convert(each, new RoleDto())).toList();
        }
        else if(loggedUser.getRole().getDescription().equals("Admin")) {
            roles= list.stream().filter(each -> !each.getDescription().equals("Root User")).map(each -> mapperUtil.convert(each, new RoleDto())).toList();
        }
        return roles;
    }

}

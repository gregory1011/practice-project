package com.app.service;

import com.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserService {

    User findByUsername(String username);
}

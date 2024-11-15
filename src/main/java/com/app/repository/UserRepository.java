package com.app.repository;

import com.app.dto.UserDto;
import com.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Integer countAllByCompany_IdAndRole_Description(Long id, String description);
    List<User> findAllByRole_id(Long i);
    List<User> findAllByCompany_id(Long l);
}

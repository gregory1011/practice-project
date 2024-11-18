package com.app.repository;

import com.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    @Query(value = "select count(*) from users  where company_id= ?1 and  role_id= 2", nativeQuery = true)
    Integer countAllByCompany_IdAndRoleIsAdmin(Long id); // role_id=2 for Admin
    List<User> findAllByRole_id(Long i);
    List<User> findAllByCompany_id(Long l);
}

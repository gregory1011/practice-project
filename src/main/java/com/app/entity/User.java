package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "users")
@Where(clause = "is_deleted=false")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String confirmPassword;

    private String firstname;

    private String lastname;

    private String phone;

    private boolean enabled;

    @ManyToOne()
    private Role role;

    @ManyToOne()
    private Company company;
}

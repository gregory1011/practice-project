package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

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

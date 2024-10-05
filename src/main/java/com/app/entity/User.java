package com.app.entity;

import com.app.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


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

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;
}

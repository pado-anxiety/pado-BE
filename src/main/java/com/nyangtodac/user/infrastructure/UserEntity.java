package com.nyangtodac.user.infrastructure;

import com.nyangtodac.user.domain.LoginType;
import com.nyangtodac.user.domain.User;
import jakarta.persistence.*;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Enumerated
    private LoginType loginType;

    private String name;

    private String refreshToken;

    protected UserEntity() {
    }

    public UserEntity(Long id, String email, LoginType loginType, String name) {
        this.id = id;
        this.email = email;
        this.loginType = loginType;
        this.name = name;
    }

    public User toModel() {
        return new User(id, email, loginType, name, refreshToken);
    }

    public static UserEntity fromModel(User user) {
        return new UserEntity(user.getId(), user.getEmail(), user.getLoginType(), user.getName());
    }
}

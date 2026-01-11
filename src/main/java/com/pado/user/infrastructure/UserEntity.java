package com.pado.user.infrastructure;

import com.pado.user.domain.LoginType;
import com.pado.user.domain.User;
import jakarta.persistence.*;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String name;

    private String refreshToken;

    protected UserEntity() {
    }

    private UserEntity(Long id, String email, LoginType loginType, String name, String refreshToken) {
        this.id = id;
        this.email = email;
        this.loginType = loginType;
        this.name = name;
        this.refreshToken = refreshToken;
    }

    public User toModel() {
        return new User(id, email, loginType, name, refreshToken);
    }

    public static UserEntity fromModel(User user) {
        return new UserEntity(user.getId(), user.getEmail(), user.getLoginType(), user.getName(), user.getRefreshToken());
    }
}

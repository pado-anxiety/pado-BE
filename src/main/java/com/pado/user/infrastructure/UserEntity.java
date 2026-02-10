package com.pado.user.infrastructure;

import com.pado.user.domain.LoginType;
import com.pado.user.domain.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @Column(nullable = false)
    private String sub;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @Column(nullable = false)
    private String name;

    private String refreshToken;

    private String oAuthRefreshToken;

    private Instant lastLoginTime;

    private String timezone;

    protected UserEntity() {
    }

    private UserEntity(Long id, String email, String sub, LoginType loginType, String name, String refreshToken, String oAuthRefreshToken, Instant lastLoginTime, String timezone) {
        this.id = id;
        this.email = email;
        this.sub = sub;
        this.loginType = loginType;
        this.name = name;
        this.refreshToken = refreshToken;
        this.oAuthRefreshToken = oAuthRefreshToken;
        this.lastLoginTime = lastLoginTime;
        this.timezone = timezone;
    }

    public User toModel() {
        return new User(id, email, sub, loginType, name, refreshToken, oAuthRefreshToken, lastLoginTime, timezone);
    }

    public static UserEntity fromModel(User user) {
        return new UserEntity(user.getId(), user.getEmail(), user.getSub(), user.getLoginType(), user.getName(), user.getRefreshToken(), user.getOAuthRefreshToken(), user.getLastLoginTime(), user.getTimezone());
    }
}

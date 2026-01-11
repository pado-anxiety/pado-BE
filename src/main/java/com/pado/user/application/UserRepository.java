package com.pado.user.application;

import com.pado.user.domain.LoginType;
import com.pado.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmailAndLoginType(String email, LoginType loginType);

    User save(User user);

    Optional<User> findByUserId(Long userId);
}

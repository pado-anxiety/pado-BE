package com.nyangtodac.user.application;

import com.nyangtodac.user.domain.LoginType;
import com.nyangtodac.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmailAndLoginType(String email, LoginType loginType);

    User save(User user);

    Optional<User> findByUserId(Long userId);
}

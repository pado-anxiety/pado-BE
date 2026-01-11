package com.pado.user.infrastructure;

import com.pado.user.application.UserRepository;
import com.pado.user.domain.LoginType;
import com.pado.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findByEmailAndLoginType(String email, LoginType loginType) {
        return userJpaRepository.findByEmailAndLoginType(email, loginType).map(UserEntity::toModel);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.fromModel(user)).toModel();
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        return userJpaRepository.findById(userId).map(UserEntity::toModel);
    }
}

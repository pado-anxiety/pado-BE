package com.nyangtodac.user.infrastructure;

import com.nyangtodac.user.domain.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmailAndLoginType(String email, LoginType loginType);
}

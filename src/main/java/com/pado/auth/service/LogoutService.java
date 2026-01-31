package com.pado.auth.service;

import com.pado.user.application.UserNotFoundException;
import com.pado.user.application.UserRepository;
import com.pado.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService {

    private final UserRepository userRepository;

    public void logout(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.updateRefreshToken(null);
        userRepository.save(user);
    }
}

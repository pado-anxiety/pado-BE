package com.pado.user.application;

import com.pado.user.controller.dto.UserInfoResponse;
import com.pado.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = getUser(userId);
        return new UserInfoResponse(user.getEmail(), user.getName());
    }

    public void updateUserInfo(Long userId, String timezone) {
        User user = getUser(userId);
        user.updateLastLoginTime();
        user.updateTimezone(timezone);
        userRepository.save(user);
    }

    public void updateMarketingConsent(Long userId, boolean agreed) {
        User user = getUser(userId);
        user.updateMarketingConsent(agreed);
        userRepository.save(user);
    }

    private User getUser(Long userId) {
        return userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }
}

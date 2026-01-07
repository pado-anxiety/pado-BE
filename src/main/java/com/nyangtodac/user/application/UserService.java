package com.nyangtodac.user.application;

import com.nyangtodac.user.controller.dto.UserInfoResponse;
import com.nyangtodac.user.domain.User;
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
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return new UserInfoResponse(user.getEmail(), user.getName());
    }

}

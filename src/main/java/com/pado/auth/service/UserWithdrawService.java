package com.pado.auth.service;

import com.pado.auth.infrastructure.oauth.apple.AppleOAuthRevoker;
import com.pado.auth.infrastructure.oauth.google.GoogleOAuthRevoker;
import com.pado.auth.infrastructure.oauth.kakao.KakaoOAuthRevoker;
import com.pado.user.application.UserNotFoundException;
import com.pado.user.application.UserRepository;
import com.pado.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWithdrawService {

    private final UserRepository userRepository;
    private final UserDataDeleteService userDataDeleteService;

    private final KakaoOAuthRevoker kakaoOAuthRevoker;
    private final AppleOAuthRevoker appleOAuthRevoker;
    private final GoogleOAuthRevoker googleOAuthRevoker;

    public void withdraw(Long userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException(userId));
        revoke(user);
        userDataDeleteService.deleteAllUserData(userId);
    }

    private void revoke(User user) {
        switch (user.getLoginType()) {
            case APPLE -> appleOAuthRevoker.revoke(user.getId(), user.getOAuthRefreshToken());
            case KAKAO -> kakaoOAuthRevoker.revoke(user.getId(), user.getSub());
            case GOOGLE -> googleOAuthRevoker.revoke(user.getId(), user.getOAuthRefreshToken());
        }
    }
}

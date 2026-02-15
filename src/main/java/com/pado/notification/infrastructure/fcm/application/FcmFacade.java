package com.pado.notification.infrastructure.fcm.application;

import com.pado.notification.infrastructure.fcm.infrastructure.FcmClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmFacade {

    private final FcmTxService fcmTxService;
    private final FcmClient fcmClient;

    @Async("fcmExecutor")
    public void sendTo(Long userId, String title, String body) {
        String token = fcmTxService.getToken(userId);
        try {
            fcmClient.sendMessageTo(userId, token, title, body);
        } catch (InvalidFcmTokenException e) {
            fcmTxService.deactivateToken(userId);
        }
    }

}

package com.pado.fcm.application;

import com.pado.fcm.infrastructure.FcmEntity;
import com.pado.fcm.infrastructure.FcmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmRepository fcmRepository;

    public void saveToken(Long userId, String fcmToken) {
        fcmRepository.save(new FcmEntity(userId, fcmToken));
    }

    public void sendMessageTo(Long userId, String title, String body) {
        FcmEntity fcm = fcmRepository.findByUserId(userId).orElseThrow(() -> new FcmNotFoundException(userId));

        FcmSendRequest request = new FcmSendRequest(
                FcmSendRequest.Message.builder()
                        .token(fcm.getToken())
                        .notification(
                                FcmSendRequest.Notification.builder()
                                        .title(title)
                                        .body(body)
                                        .build())
                        .build()
        );

    }
}

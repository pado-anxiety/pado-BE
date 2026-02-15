package com.pado.notification.infrastructure.fcm.infrastructure;

import com.google.firebase.messaging.*;
import com.pado.notification.infrastructure.fcm.application.InvalidFcmTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FcmClient {

    public void sendMessageTo(Long userId, String token, String title, String body) {
        try {
            sendMessage(token, title, body);
        } catch (FirebaseMessagingException e) {
            MessagingErrorCode errorCode = e.getMessagingErrorCode();
            if (isInvalidToken(errorCode)) {
                log.warn("Invalid FCM token. userId={}", userId);
                throw new InvalidFcmTokenException();
            } else {
                log.warn("Unknown FCM error occurred. userId={}, errorCode={}, errorMessage={}", userId, e.getMessagingErrorCode(), e.getMessage());
            }
        }
    }

    private void sendMessage(String token, String title, String body) throws FirebaseMessagingException {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build()
                )
                .build();
        FirebaseMessaging.getInstance().send(message);
    }

    private boolean isInvalidToken(MessagingErrorCode code) {
        return code == MessagingErrorCode.UNREGISTERED || code == MessagingErrorCode.INVALID_ARGUMENT || code == MessagingErrorCode.SENDER_ID_MISMATCH;
    }
}

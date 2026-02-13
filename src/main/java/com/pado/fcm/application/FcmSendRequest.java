package com.pado.fcm.application;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FcmSendRequest {

    public FcmSendRequest(Message message) {
        this.message = message;
    }

    private final Message message;

    @Builder
    public static class Message {
        private String token;
        private Notification notification;
    }

    @Builder
    public static class Notification {
        private String title;
        private String body;
    }
}

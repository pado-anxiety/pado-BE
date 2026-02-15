package com.pado.notification.controller;

import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.notification.infrastructure.fcm.application.FcmTxService;
import com.pado.notification.controller.dto.FcmSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FcmTxService fcmTxService;

    @PostMapping("/fcm")
    public ResponseEntity<Void> saveFcmToken(@LoginUser AuthUser authUser, FcmSaveRequest fcmSaveRequest) {
        fcmTxService.saveToken(authUser.getUserId(), fcmSaveRequest.getFcmToken());
        return ResponseEntity.ok().build();
    }
}

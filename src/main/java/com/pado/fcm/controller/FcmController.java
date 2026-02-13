package com.pado.fcm.controller;

import com.pado.auth.infrastructure.AuthUser;
import com.pado.auth.infrastructure.LoginUser;
import com.pado.fcm.application.FcmService;
import com.pado.fcm.controller.dto.FcmSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/fcm")
    public ResponseEntity<Void> saveFcmToken(@LoginUser AuthUser authUser, FcmSaveRequest fcmSaveRequest) {
        fcmService.saveToken(authUser.getUserId(), fcmSaveRequest.getFcmToken());
        return ResponseEntity.ok().build();
    }
}

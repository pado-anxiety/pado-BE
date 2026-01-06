package com.nyangtodac.user.controller;

import com.nyangtodac.auth.infrastructure.LoginUser;
import com.nyangtodac.user.application.UserService;
import com.nyangtodac.user.controller.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@LoginUser Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }
}

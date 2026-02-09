package com.pado.user.controller;

import com.pado.auth.infrastructure.LoginUser;
import com.pado.user.application.UserService;
import com.pado.user.controller.dto.RecentUserInfoRequest;
import com.pado.user.controller.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@LoginUser Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @PostMapping
    public ResponseEntity<Void> lastLoginAt(@LoginUser Long userId, @RequestBody RecentUserInfoRequest recentUserInfoRequest) {
        userService.updateUserInfo(userId, recentUserInfoRequest.getTimezone());
        return ResponseEntity.noContent().build();
    }
}

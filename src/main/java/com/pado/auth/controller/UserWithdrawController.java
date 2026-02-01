package com.pado.auth.controller;

import com.pado.auth.infrastructure.LoginUser;
import com.pado.auth.service.UserWithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserWithdrawController {

    private final UserWithdrawService userWithdrawService;

    @DeleteMapping("/user/withdraw")
    public ResponseEntity<Void> withdraw(@LoginUser Long userId) {
        userWithdrawService.withdraw(userId);
        return ResponseEntity.noContent().build();
    }
}

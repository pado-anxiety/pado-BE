package com.pado.auth.controller;

import com.pado.auth.infrastructure.AuthUser;
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

    @DeleteMapping("/users")
    public ResponseEntity<Void> withdraw(@LoginUser AuthUser authUser) {
        userWithdrawService.withdraw(authUser.getUserId());
        return ResponseEntity.noContent().build();
    }
}

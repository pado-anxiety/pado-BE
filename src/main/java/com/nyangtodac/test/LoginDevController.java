package com.nyangtodac.test;

import com.nyangtodac.auth.infrastructure.jwt.JwtTokenProvider;
import com.nyangtodac.user.application.UserRepository;
import com.nyangtodac.user.domain.LoginType;
import com.nyangtodac.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Profile("dev")
@RestController
@RequiredArgsConstructor
public class LoginDevController {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    private static final String TEST_USER = "test@test.com";

    @PostMapping("/dev/login")
    public ResponseEntity<DevLogin> login() {
        Optional<User> user = userRepository.findByEmailAndLoginType(TEST_USER, LoginType.GOOGLE);
        if (user.isPresent()) {
            return ResponseEntity.ok(new DevLogin(jwtTokenProvider.createAccessToken(user.get().getId())));
        }
        User save = userRepository.save(new User(TEST_USER, "test", LoginType.GOOGLE));
        return ResponseEntity.ok(new DevLogin(jwtTokenProvider.createAccessToken(save.getId())));
    }
}

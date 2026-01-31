package com.pado.test;

import com.pado.auth.infrastructure.jwt.JwtTokenProvider;
import com.pado.user.application.UserRepository;
import com.pado.user.domain.LoginType;
import com.pado.user.domain.User;
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

    private static final String TEST_USER = "testsubject";

    @PostMapping("/dev/login")
    public ResponseEntity<DevLogin> login() {
        Optional<User> user = userRepository.findBySubAndLoginType(TEST_USER, LoginType.GOOGLE);
        if (user.isPresent()) {
            return ResponseEntity.ok(new DevLogin(jwtTokenProvider.createAccessToken(user.get().getId())));
        }
        User save = userRepository.save(new User("test@test.com", TEST_USER, "test", LoginType.GOOGLE, "testOAuthRefreshToken"));
        return ResponseEntity.ok(new DevLogin(jwtTokenProvider.createAccessToken(save.getId())));
    }
}

package com.pado.chat.aes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AesGcmUtilTest {

    private static final String TEST_KEY = "/vywY964g+orkbvg7ZyRvFHxnkKLVnRORBVpyUYwgA8=";
    final AesGcmUtil util = new AesGcmUtil(TEST_KEY);

    @Test
    @DisplayName("encrypt 테스트")
    void encrypt() {
        assertDoesNotThrow(() -> util.encrypt("안녕하세요"));
    }

    @Test
    @DisplayName("decrypt 테스트")
    void decrypt() {
        String raw = "저는 오늘 생일입니다";
        String encrypted = util.encrypt(raw);
        String decrypt = util.decrypt(encrypted);

        assertThat(raw).isEqualTo(decrypt);
    }
}

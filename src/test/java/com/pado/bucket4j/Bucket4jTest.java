package com.pado.bucket4j;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class Bucket4jTest {

    private final Map<Long, Bucket> bucketStore = new ConcurrentHashMap<>();

    public Bucket resolveBucket(Long userId) {
        return bucketStore.computeIfAbsent(userId, id ->
                Bucket.builder()
                        .addLimit(limit -> limit.capacity(5).refillIntervally(1, Duration.ofSeconds(1000)))
                        .build()
        );
    }

    @BeforeEach
    void setUp() {
        bucketStore.clear();
    }

    @Test
    @DisplayName("토큰 소비 테스트")
    void test1() throws InterruptedException {
        Long user1 = 1L;

        Bucket bucket = resolveBucket(user1);

        assertThat(bucket.tryConsume(1)).isTrue();
        assertThat(bucket.getAvailableTokens()).isEqualTo(4);

        assertThat(bucket.tryConsume(1)).isTrue();
        assertThat(bucket.tryConsume(1)).isTrue();
        assertThat(bucket.tryConsume(1)).isTrue();
        assertThat(bucket.tryConsume(1)).isTrue();

        assertThat(bucket.getAvailableTokens()).isEqualTo(0);

        assertThat(bucket.tryConsume(1)).isFalse();
    }

}

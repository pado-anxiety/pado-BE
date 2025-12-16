package com.nyangtodac.cbt.recommend;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CBTHistoryRedisRepository {

    private static final String CBT_COOLDOWN_PREFIX = "cbt:cooldown:";
    private static final Duration COOLDOWN_DURATION = Duration.ofMinutes(30);

    private final StringRedisTemplate redisTemplate;

    public void setCooldown(Long userId, CBT cbt) {
        String key = generateCooldownKey(userId, cbt);
        redisTemplate.opsForValue().set(key, "1", COOLDOWN_DURATION);
    }

    public Set<CBT> getCooldownCBTs(Long userId) {
        String pattern = generateCooldownPattern(userId);
        Set<String> keys = redisTemplate.keys(pattern);
        
        if (keys.isEmpty()) {
            return Collections.emptySet();
        }
        
        Set<CBT> cooldownCBTs = new HashSet<>();
        for (String key : keys) {
            try {
                String cbtName = key.substring(key.lastIndexOf(":") + 1);
                cooldownCBTs.add(CBT.valueOf(cbtName));
            } catch (IllegalArgumentException e) {
                log.warn("Unknown CBT type in cooldown key: {}", key);
            }
        }
        
        return cooldownCBTs;
    }

    private String generateCooldownKey(Long userId, CBT cbt) {
        return CBT_COOLDOWN_PREFIX + userId + ":" + cbt.name();
    }

    private String generateCooldownPattern(Long userId) {
        return CBT_COOLDOWN_PREFIX + userId + ":*";
    }
}
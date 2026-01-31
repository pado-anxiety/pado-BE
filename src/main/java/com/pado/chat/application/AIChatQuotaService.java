package com.pado.chat.application;

import com.pado.chat.quota.QuotaStatus;
import com.pado.chat.quota.UserBucketConfig;
import com.pado.config.properties.ChatQuotaProperties;
import io.github.bucket4j.EstimationProbe;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIChatQuotaService {

    private final ProxyManager<String> proxyManager;
    private final UserBucketConfig userBucketConfig;
    private final ChatQuotaProperties chatQuotaProperties;

    public boolean tryConsume(Long userId) {
        return proxyManager
                .builder()
                .build(String.valueOf(userId), userBucketConfig::getConfig)
                .tryConsume(1L);
    }

    public QuotaStatus getQuotaStatus(Long userId) {
        BucketProxy bucketProxy = proxyManager
                .builder()
                .build(String.valueOf(userId), userBucketConfig::getConfig);

        long remaining = bucketProxy.getAvailableTokens();

        long nanosToNextToken;
        if (remaining >= chatQuotaProperties.getMaxTokens()) {
            nanosToNextToken = 0;
        } else {
            EstimationProbe nextRefillProbe = bucketProxy.estimateAbilityToConsume(remaining + 1);
            nanosToNextToken = nextRefillProbe.getNanosToWaitForRefill();
        }

        return new QuotaStatus(nanosToNextToken, remaining);
    }
}

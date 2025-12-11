package com.nyangtodac.chat.application;

import com.nyangtodac.chat.quota.QuotaStatus;
import com.nyangtodac.chat.quota.UserBucketConfig;
import io.github.bucket4j.EstimationProbe;
import io.github.bucket4j.distributed.BucketProxy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIQuotaService {

    private final ProxyManager<String> proxyManager;

    public boolean tryConsume(Long userId) {
        return proxyManager
                .builder()
                .build(String.valueOf(userId), UserBucketConfig::getConfig)
                .tryConsume(1L);
    }

    public QuotaStatus getQuotaStatus(Long userId) {
        BucketProxy bucketProxy = proxyManager
                .builder()
                .build(String.valueOf(userId), UserBucketConfig::getConfig);

        long remaining = bucketProxy.getAvailableTokens();

        long nanosToNextToken;
        if (remaining >= UserBucketConfig.MAX_TOKEN) {
            nanosToNextToken = 0;
        } else {
            EstimationProbe nextRefillProbe = bucketProxy.estimateAbilityToConsume(remaining + 1);
            nanosToNextToken = nextRefillProbe.getNanosToWaitForRefill();
        }

        return new QuotaStatus(nanosToNextToken, remaining);
    }
}

package com.pado.chat.mq;

import com.pado.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.springframework.amqp.rabbit.connection.CorrelationData.Confirm;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingFlushProducer {

    private static final int MAX_ATTEMPT = 3;

    private final RabbitTemplate rabbitTemplate;
    private final ChattingFallbackService fallbackService;

    public void publish(Long userId, List<Chatting> chattings) {
        String correlationId = UUID.randomUUID().toString();
        if (tryPublish(correlationId, userId, chattings)) return;
        log.warn("MQ publish failed. trigger fallback. correlationId={}, userId={}", correlationId, userId);
        fallbackService.fallback(userId, chattings);
    }

    private boolean tryPublish(String correlationId, Long userId, List<Chatting> chattings) {
        for (int attempt = 1; attempt <= MAX_ATTEMPT; attempt++) {
            try {
                CorrelationData correlationData = new CorrelationData(correlationId);
                rabbitTemplate.convertAndSend(
                        ChattingRabbitMqConfig.EXCHANGE,
                        ChattingRabbitMqConfig.FLUSH_QUEUE,
                        new ChattingPersistMessage(userId, chattings),
                        correlationData
                );
                Confirm confirm = correlationData.getFuture().get(5, TimeUnit.SECONDS);
                if (confirm.isAck()) return true;
                log.warn("MQ publish retry. correlationId={}, attempt={}/{}, userId={}, cause={}", correlationId, attempt, MAX_ATTEMPT, userId, confirm.getReason());
            } catch (TimeoutException e) {
                log.warn("MQ publish timeout. correlationId={}, attempt={}/{}", correlationId, attempt, MAX_ATTEMPT);
            } catch (Exception e) {
                log.error("MQ publish exception. correlationId={}, attempt={}/{}", correlationId, attempt, MAX_ATTEMPT, e);
            }
        }
        return false;
    }
}

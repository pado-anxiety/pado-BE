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

    private final RabbitTemplate rabbitTemplate;

    public boolean publish(Long userId, List<Chatting> chattings) {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        try {
            rabbitTemplate.convertAndSend(
                    ChattingRabbitMqConfig.EXCHANGE,
                    ChattingRabbitMqConfig.FLUSH_QUEUE,
                    new ChattingPersistMessage(userId, chattings),
                    correlationData
            );

            Confirm confirm = correlationData.getFuture().get(5, TimeUnit.SECONDS);

            if (!confirm.isAck()) {
                log.error("MQ publish nack. cause={}", confirm.getReason());
                return false;
            }
            return true;
        } catch (TimeoutException e) {
            log.error("MQ Publish timeout. userId={}", userId, e);
            return false;
        } catch (Exception e) {
            log.error("MQ Publish failed with exception. userId={}", userId, e);
            return false;
        }
    }
}

package com.pado.chat.mq;

import com.pado.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChattingFlushProducer {

    private final RabbitTemplate rabbitTemplate;

    public boolean publish(Long userId, List<Chatting> chattings) {
        try {
            for (Chatting chatting : chattings) {
                rabbitTemplate.convertAndSend(
                        ChattingRabbitMqConfig.EXCHANGE,
                        ChattingRabbitMqConfig.FLUSH_QUEUE,
                        new ChattingPersistMessage(userId, chatting)
                );
            }
            return true;
        } catch (Exception e) {
            log.error("chat flush failed. userId={}", userId, e);
            return false;
        }
    }
}

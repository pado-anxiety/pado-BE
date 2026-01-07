package com.nyangtodac.external.rabbitmq;

import com.nyangtodac.chat.domain.Chatting;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChattingFlushProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publish(Long userId, List<Chatting> chattings) {
        for (Chatting chatting : chattings) {
            rabbitTemplate.convertAndSend(
                    RabbitMqConfig.EXCHANGE,
                    RabbitMqConfig.FLUSH_QUEUE,
                    new ChattingPersistMessage(userId, chatting)
            );
        }
    }
}

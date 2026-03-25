package com.pado.chat.mq;

import com.pado.chat.infrastructure.ChattingDBRepository;
import com.rabbitmq.client.Channel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChattingFlushConsumer {

    private final ChattingDBRepository chatRepository;

    @RabbitListener(queues = ChattingRabbitMqConfig.FLUSH_QUEUE, ackMode = "MANUAL")
    @Transactional
    public void consume(ChattingPersistMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            chatRepository.saveAll(message.getUserId(), message.getChattings());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Chat persist failed. userId={}, payload={}, reason={}", message.getUserId(), message, e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}

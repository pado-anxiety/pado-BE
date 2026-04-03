package com.pado.chat.mq;

import com.pado.chat.infrastructure.ChattingDBRepository;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChattingFlushConsumer {

    private final ChattingDBRepository chatRepository;
    private final TransactionTemplate transactionTemplate;

    @RabbitListener(queues = ChattingRabbitMqConfig.FLUSH_QUEUE, ackMode = "MANUAL")
    public void consume(ChattingPersistMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            transactionTemplate.executeWithoutResult(status -> chatRepository.upsertAll(message.getUserId(), message.getChattings()));
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Chat persist failed. userId={}, reason={}", message.getUserId(), e.getMessage());
            channel.basicNack(tag, false, false);
        }
    }
}

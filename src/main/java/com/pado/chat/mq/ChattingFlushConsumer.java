package com.pado.chat.mq;

import com.pado.chat.infrastructure.ChattingDBRepository;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChattingFlushConsumer {

    private final ChattingDBRepository chatRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = ChattingRabbitMqConfig.FLUSH_QUEUE, ackMode = "MANUAL")
    public void consume(ChattingPersistMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            chatRepository.save(message.getUserId(), message.getChatting());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            rabbitTemplate.convertAndSend(
                    ChattingRabbitMqConfig.EXCHANGE,
                    ChattingRabbitMqConfig.DEAD_QUEUE,
                    message,
                    msg -> {
                        msg.getMessageProperties().setHeader("failReason", e.getMessage());
                        msg.getMessageProperties().setHeader("failedAt", Instant.now());
                        return msg;
                    }
            );
            channel.basicAck(tag, false);
        }
    }
}

package com.pado.external.rabbitmq;

import com.pado.chat.infrastructure.ChattingDBRepository;
import com.rabbitmq.client.Channel;
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
public class ChatFlushFailedConsumer {

    private final ChattingDBRepository chatRepository;

    @RabbitListener(queues = RabbitMqConfig.FAILED_QUEUE, ackMode = "MANUAL")
    public void consume(ChattingPersistMessage message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            chatRepository.save(message.getUserId(), message.getChatting());
            log.info("Chatting flush retry succeeded. userId={}, tsid={}", message.getUserId(), message.getChatting().getTsid());
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Chatting flush retry failed. userId={}, tsid={}", message.getUserId(), message.getChatting().getTsid(), e);
            channel.basicReject(tag, false);
        }
    }
}

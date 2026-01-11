package com.pado.external.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE = "chat.flush.ex";
    public static final String FLUSH_QUEUE = "chat.flush";
    public static final String FAILED_QUEUE = "chat.failed";
    public static final String DEAD_QUEUE = "chat.dead";

    @Bean
    public ConnectionFactory connectionFactory(
            @Value("${spring.rabbitmq.port}") int port,
            @Value("${spring.rabbitmq.host}") String host,
            @Value("${spring.rabbitmq.username}") String username,
            @Value("${spring.rabbitmq.password}") String password) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setPort(port);
        connectionFactory.setHost(host);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setBeforePublishPostProcessors(message -> {
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });
        return template;
    }

    @Bean
    public DirectExchange chatExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue flushQueue() {
        return QueueBuilder.durable(FLUSH_QUEUE)
                .deadLetterExchange(EXCHANGE)
                .deadLetterRoutingKey(FAILED_QUEUE)
                .build();
    }

    @Bean
    public Queue failedQueue() {
        return QueueBuilder.durable(FAILED_QUEUE)
                .deadLetterExchange(EXCHANGE)
                .deadLetterRoutingKey(DEAD_QUEUE)
                .build();
    }

    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    @Bean
    public Binding flushBinding() {
        return BindingBuilder.bind(flushQueue())
                .to(chatExchange())
                .with(FLUSH_QUEUE);
    }

    @Bean
    public Binding failedBinding() {
        return BindingBuilder.bind(failedQueue())
                .to(chatExchange())
                .with(FAILED_QUEUE);
    }

    @Bean
    public Binding deadBinding() {
        return BindingBuilder.bind(deadQueue())
                .to(chatExchange())
                .with(DEAD_QUEUE);
    }

}

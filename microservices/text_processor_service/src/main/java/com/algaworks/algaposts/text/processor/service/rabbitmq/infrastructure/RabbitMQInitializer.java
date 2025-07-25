package com.algaworks.algaposts.text.processor.service.rabbitmq.infrastructure;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMQInitializer {
    private final RabbitAdmin rabbitAdmin;

    @PostConstruct
    public void initialize() {
        rabbitAdmin.initialize();
    }
}

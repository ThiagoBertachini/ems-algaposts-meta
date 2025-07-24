package com.algaworks.algaposts.text.processor.service.rabbitmq.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String POST_PROCESS_EXCHANGE = "post-processing.text-received.v1.e";
    public static final String PROCESS_POST = "post-service.post-processing-result.v1";
    public static final String QUEUE_PROCESS_POST = PROCESS_POST + ".q";
    public static final String DLQ_PROCESS_POST = PROCESS_POST +".dlq";

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public Queue queueProcessPost() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", DLQ_PROCESS_POST);

        return QueueBuilder.durable(QUEUE_PROCESS_POST)
                .withArguments(args).build();
    }

//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return ExchangeBuilder
//                .fanoutExchange(POST_PROCESS_EXCHANGE)
//                .build();
//    }
//
//    public Binding bindingProcessPost() {
//        return BindingBuilder
//                .bind(queueProcessPost())
//                .to(fanoutExchange());
//    }

    @Bean
    public Queue dlqProcessPost() {
        return QueueBuilder.durable(DLQ_PROCESS_POST).build();
    }
}
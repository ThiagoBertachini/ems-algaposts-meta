package com.algaworks.algaposts.post.service.rabbitmq.infrastructure;

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

    public static final String TEXT_PROCESS_EXCHANGE = "text-processing.text-received.v1.e";
    public static final String PROCESS_TEXT = "text-processor-service.post-processing.v1";
    public static final String QUEUE_PROCESS_TEXT = PROCESS_TEXT + ".q";
    public static final String DLQ_TEXT_POST = PROCESS_TEXT +".dlq";

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
        args.put("x-dead-letter-routing-key", DLQ_TEXT_POST);

        return QueueBuilder.durable(QUEUE_PROCESS_TEXT)
                .withArguments(args).build();
    }

//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return ExchangeBuilder
//                .fanoutExchange(TEXT_PROCESS_EXCHANGE)
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
        return QueueBuilder.durable(DLQ_TEXT_POST).build();
    }

}

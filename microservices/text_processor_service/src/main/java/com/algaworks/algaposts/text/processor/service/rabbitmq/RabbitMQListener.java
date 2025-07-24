package com.algaworks.algaposts.text.processor.service.rabbitmq;

import com.algaworks.algaposts.text.processor.service.rabbitmq.dto.ProcessPostInputLogData;
import com.algaworks.algaposts.text.processor.service.rabbitmq.dto.ProcessPostResultLogData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.algaworks.algaposts.text.processor.service.rabbitmq.infrastructure.RabbitMQConfig.POST_PROCESS_EXCHANGE;
import static com.algaworks.algaposts.text.processor.service.rabbitmq.infrastructure.RabbitMQConfig.QUEUE_PROCESS_POST;

@Slf4j
@RequiredArgsConstructor
@Component
public class RabbitMQListener {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "text-processor-service.post-processing.v1.q", concurrency = "2-3")
    public void handleProcessPost(@Payload ProcessPostInputLogData processPostInputLogData) {

        final int wordCount = countWords(processPostInputLogData.getPostBody());
        final Double calculatedValue = wordCount * 0.10;

        final String routingKey = "";

        ProcessPostResultLogData processPostResultLogData =
                ProcessPostResultLogData.builder()
                        .postId(processPostInputLogData.getPostId())
                        .wordCount(wordCount)
                        .calculatedValue(calculatedValue)
                        .build();

        rabbitTemplate.convertAndSend(QUEUE_PROCESS_POST, processPostResultLogData);

        log.info("Processed postId={} | wordCount={} | value={}",
                processPostResultLogData.getPostId(), wordCount, calculatedValue);
    }

    private int countWords(String text) {
        return text.split("\\s+").length;
    }
}

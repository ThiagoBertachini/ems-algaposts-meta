package com.algaworks.algaposts.post.service.rabbitmq;

import com.algaworks.algaposts.post.service.domain.model.Post;
import com.algaworks.algaposts.post.service.domain.repositories.PostServiceRepository;
import com.algaworks.algaposts.post.service.rabbitmq.dto.ProcessPostResultLogData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RabbitMQListener {

    private final PostServiceRepository postServiceRepository;

    @RabbitListener(queues = "post-service.post-processing-result.v1.q", concurrency = "2-3")
    public void handleProcessPost(@Payload ProcessPostResultLogData processPostResultLogData) {
        log.info("Post received - Updating post {}",
                processPostResultLogData.getPostId());

        Post postData = postServiceRepository.findById(processPostResultLogData.getPostId())
                        .orElseThrow(() -> new RuntimeException("Post not found"));

        postData.setWordCount(processPostResultLogData.getWordCount());
        postData.setCalculatedValue(processPostResultLogData.getCalculatedValue());

        postServiceRepository.save(postData);
    }
}

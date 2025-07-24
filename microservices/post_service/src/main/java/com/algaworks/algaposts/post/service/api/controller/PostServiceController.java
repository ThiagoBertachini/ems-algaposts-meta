package com.algaworks.algaposts.post.service.api.controller;

import com.algaworks.algaposts.post.service.api.model.PostInput;
import com.algaworks.algaposts.post.service.api.model.PostOutput;
import com.algaworks.algaposts.post.service.api.model.PostSummaryOutput;
import com.algaworks.algaposts.post.service.domain.model.Post;
import com.algaworks.algaposts.post.service.domain.repositories.PostServiceRepository;
import com.algaworks.algaposts.post.service.rabbitmq.dto.ProcessPostInputLogData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.algaworks.algaposts.post.service.rabbitmq.infrastructure.RabbitMQConfig.QUEUE_PROCESS_TEXT;
import static com.algaworks.algaposts.post.service.rabbitmq.infrastructure.RabbitMQConfig.TEXT_PROCESS_EXCHANGE;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/posts")
public class PostServiceController {

    private final RabbitTemplate rabbitTemplate;
    private final PostServiceRepository postServiceRepository;


    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PostOutput createPost(@RequestBody @Valid PostInput postInput) {
        if (postInput == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Post postData = Post.builder()
                .id(UUID.randomUUID())
                .author(postInput.getAuthor())
                .title(postInput.getTitle())
                .body(postInput.getBody()).build();

        ProcessPostInputLogData processPostInputLogData =
                ProcessPostInputLogData.builder()
                        .postId(postData.getId())
                        .postBody(postData.getBody())
                        .build();

        final String routingKey = "";

        // Enviar para fila
        rabbitTemplate.convertAndSend(QUEUE_PROCESS_TEXT, processPostInputLogData);

        // salvar h2
        // retornar 201
        return postServiceRepository.save(postData).toPostOutput();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostOutput getPostById(@PathVariable UUID id) {
        return postServiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .toPostOutput();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PostSummaryOutput> getAllPosts(Pageable pageable) {
        // Retornar 200
        return postServiceRepository.findAll(pageable).map(Post::toPostSummaryOutput);
    }
}
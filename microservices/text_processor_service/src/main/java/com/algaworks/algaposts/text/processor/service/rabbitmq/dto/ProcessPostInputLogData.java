package com.algaworks.algaposts.text.processor.service.rabbitmq.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProcessPostInputLogData {
    private UUID postId;
    private String postBody;
}

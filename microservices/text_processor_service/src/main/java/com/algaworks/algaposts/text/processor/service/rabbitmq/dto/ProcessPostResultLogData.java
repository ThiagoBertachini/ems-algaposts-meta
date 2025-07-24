package com.algaworks.algaposts.text.processor.service.rabbitmq.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class ProcessPostResultLogData {
    private UUID postId;
    private Integer wordCount;
    private Double calculatedValue;
}

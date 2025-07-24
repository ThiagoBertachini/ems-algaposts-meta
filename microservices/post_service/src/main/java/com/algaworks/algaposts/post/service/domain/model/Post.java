package com.algaworks.algaposts.post.service.domain.model;

import com.algaworks.algaposts.post.service.api.model.PostOutput;
import com.algaworks.algaposts.post.service.api.model.PostSummaryOutput;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Post {
    @Id
    private UUID id;
    private String title;
    private String body;
    private String author;
    private Integer wordCount;
    private Double calculatedValue;

    public PostOutput toPostOutput() {
        return PostOutput.builder()
                .id(id)
                .title(title)
                .body(body)
                .author(author)
                .wordCount(wordCount)
                .calculatedValue(calculatedValue)
                .build();
    }

    public PostSummaryOutput toPostSummaryOutput() {
        return PostSummaryOutput.builder()
                .title(title)
                .author(author)
                .summary(body)
                .build();
    }
}

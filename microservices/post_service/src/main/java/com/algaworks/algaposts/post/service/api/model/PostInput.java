package com.algaworks.algaposts.post.service.api.model;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
@Builder
public class PostInput {

  @NotEmpty(message = "O título não pode ser vazio")
  private String title;
  @NotEmpty(message = "O corpo não pode ser vazio")
  private String body;
  @NotEmpty(message = "O autor não pode ser vazio")
  private String author;
}
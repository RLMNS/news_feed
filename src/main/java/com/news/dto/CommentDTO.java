package com.news.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;

    @NotBlank(message = "Text is required")
    private String text;

    @NotBlank(message = "Username is required")
    private String username;
    private LocalDateTime createdAt;
}

package com.news.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NewsDTO {
    private Long id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Content is required")
    private String content;

    private List<Long> images;

    private List<CommentDTO> comments;
    private ThemeDTO theme;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 
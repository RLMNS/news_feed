package com.news.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ThemeDTO {
    private Long id;

    @NotBlank(message = "Theme name is required")
    private String themeName;
}

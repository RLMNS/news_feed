package com.news.controller;

import com.news.dto.NewsCreateDTO;
import com.news.dto.NewsDTO;
import com.news.service.NewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/news")
@PreAuthorize("hasRole('EDITOR')")
@RequiredArgsConstructor
@Tag(name = "News Controller", description = "API для управления новостями")
@CrossOrigin(origins = "*")
public class NewsController {
    private final NewsService newsService;

    @Operation(summary = "Создать новую новость", description = "Создает новую новость с указанными данными")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Новость успешно создана"),
        @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<NewsDTO> createNews(@RequestBody @Valid NewsCreateDTO newsCreateDTO) throws IOException {
        return ResponseEntity.ok(newsService.createNews(newsCreateDTO));
    }

    @Operation(summary = "Обновить новость", description = "Обновляет существующую новость по ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Новость успешно обновлена"),
        @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
        @ApiResponse(responseCode = "404", description = "Новость не найдена"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NewsDTO> updateNews(@PathVariable Long id, @RequestBody @Valid NewsCreateDTO newsDTO) {
        return ResponseEntity.ok(newsService.updateNews(id, newsDTO));
    }

    @Operation(summary = "Удалить новость", description = "Удаляет новость по указанному ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Новость успешно удалена"),
        @ApiResponse(responseCode = "404", description = "Новость не найдена"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EDITOR')")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.ok().build();
    }
} 
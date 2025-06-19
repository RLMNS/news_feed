package com.news.controller;

import com.news.dto.CommentDTO;
import com.news.dto.NewsDTO;
import com.news.dto.ThemeDTO;
import com.news.model.Image;
import com.news.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Tag(name = "Public Controller", description = "Публичные API для новостей и комментариев")
@CrossOrigin(origins = "*")
public class PublicController {
    private final NewsService newsService;
    private final CommentService commentService;
    private final ImageService imageService;
    private final ThemeService themeService;

    @Operation(summary = "Получить все новости", description = "Возвращает постраничный список всех новостных статей.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Список новостей успешно получен"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/news")
    public ResponseEntity<Page<NewsDTO>> getAllNews(Pageable pageable) {
        return ResponseEntity.ok(newsService.getAllNews(pageable));
    }

    @Operation(summary = "Получайте новости по идентификатору", description = "Возвращает новостную статью по указанному идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Новость найдена"),
        @ApiResponse(responseCode = "404", description = "Новость не найдена"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/news/{id}")
    public ResponseEntity<NewsDTO> getNewsById(
            @Parameter(description = "News ID", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @Operation(summary = "Получить все комментарии к новостной статье",
            description = "Возвращает постраничные комментарии для определенной новостной статьи")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Комментарии успешно получены"),
        @ApiResponse(responseCode = "404", description = "Новость не найдена"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/news/{newsId}/comments")
    public ResponseEntity<Page<CommentDTO>> getCommentsByNewsId(
            @Parameter(description = "News ID", required = true) @PathVariable Long newsId,
            Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByNewsId(newsId, pageable));
    }

    @Operation(summary = "Получить изображение", description = "Получает изображение по идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Изображение найдено"),
        @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    })
    @GetMapping("/images/{id}")
    public ResponseEntity<byte[]> getImage(
            @Parameter(description = "Image ID", required = true) @PathVariable Long id) {
        Image image = imageService.getImage(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(image.getData());
    }

    @Operation(summary = "Получить все комментарии к новостной статье",
            description = "Возвращает постраничные комментарии для определенной новостной статьи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарии успешно получены"),
            @ApiResponse(responseCode = "404", description = "Новость не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/news/themes")
    public ResponseEntity<List<ThemeDTO>> getAllThemes() {
        return ResponseEntity.ok(themeService.getAllThemes());
    }

    @Operation(summary = "Получить все комментарии по имени пользователя",
            description = "Возвращает постраничные комментарии для пользователя по имени пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарии успешно получены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{username}/comments")
    public ResponseEntity<Page<CommentDTO>> getCommentsByCurrentUser(@Parameter(description = "username", required = true)
                                                                     @PathVariable String username, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByUsername(username, pageable));
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
} 
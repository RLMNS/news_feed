package com.news.controller;

import com.news.dto.CommentDTO;
import com.news.model.Comment;
import com.news.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/comments")
@PreAuthorize("hasRole('READER')")
@RequiredArgsConstructor
@Tag(name = "Comment Controller", description = "APIs для работы с комментариями")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/news/{newsId}")
    @Operation(summary = "Создать комментарий для новости")
    public ResponseEntity<CommentDTO> createComment(
            @PathVariable @NotNull(message = "Укажите идентификатор новости") Long newsId,
            @RequestBody @NotBlank(message = "Требуется текст комментария") String text) {
        return ResponseEntity.ok(commentService.createComment(newsId, text));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Удалить комментарий")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получить все комментарии текущего пользователя",
            description = "Возвращает постраничные комментарии для текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарии успешно получены"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/user/comments")
    public ResponseEntity<Page<CommentDTO>> getCommentsByCurrentUser(Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByCurrentUser(pageable));
    }
} 
package com.news.controller;

import com.news.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/images")
@PreAuthorize("hasRole('EDITOR')")
@RequiredArgsConstructor
@Tag(name = "Image Controller", description = "API для работы с изображениями")
public class ImageController {
    private final ImageService imageService;

    @Operation(summary = "Загрузить изображение", description = "Загружает JPEG изображение в базу данных")
    @ApiResponse(responseCode = "200", description = "Изображение успешно загружено")
    @ApiResponse(responseCode = "400", description = "Неверный формат файла")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Long> uploadImage(@RequestParam Long newsId,
                                            @RequestParam("file") MultipartFile file) throws IOException {
        Long imageId = imageService.saveImage(file, newsId);
        return ResponseEntity.ok(imageId);
    }

    @Operation(summary = "Удалить изображение", description = "Удаляет изображение по ID")
    @ApiResponse(responseCode = "200", description = "Изображение успешно удалено")
    @ApiResponse(responseCode = "404", description = "Изображение не найдено")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }
} 
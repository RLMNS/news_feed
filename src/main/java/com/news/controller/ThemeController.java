package com.news.controller;

import com.news.dto.ThemeDTO;
import com.news.repository.ThemeRepository;
import com.news.service.ThemeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/themes")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Theme Controller", description = "APIs для работы с темами новостей")
public class ThemeController {
    private final ThemeService themeService;

    @Operation(summary = "Создать новую тему", description = "Создает новую тему с указанными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Тема успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<ThemeDTO> createTheme(@RequestBody @NotBlank(message = "Text is required") String themeName) {
        return ResponseEntity.ok(themeService.createTheme(themeName));
    }

    @Operation(summary = "Обновить тему", description = "Обновляет существующую тему по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тема успешно обновлена"),
            @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
            @ApiResponse(responseCode = "404", description = "Тема не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ThemeDTO> updateTheme(@PathVariable Long id,
                                                @RequestBody @NotBlank(message = "Text is required") String themeName) {
        return ResponseEntity.ok(themeService.updateTheme(id, themeName));
    }

    @Operation(summary = "Удалить тему", description = "Удаляет тему по указанному ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Тема успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Тема не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.ok().build();
    }
}

package com.news.controller;

import com.news.dto.LoginRequestDTO;
import com.news.dto.TokenResponseDTO;
import com.news.dto.UserRegistrationDTO;
import com.news.service.KeycloakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "API для аутентификации и регистрации")
@CrossOrigin(origins = "*")
public class AuthController {

    private final KeycloakService keycloakService;

    @Operation(summary = "Регистрация нового пользователя", description = "Создает нового пользователя в системе")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
        @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
        @ApiResponse(responseCode = "409", description = "Пользователь с таким именем уже существует"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegistrationDTO registrationDTO) {
        keycloakService.registerUser(
            registrationDTO.getUsername(),
            registrationDTO.getEmail(),
            registrationDTO.getPassword(),
            registrationDTO.getFirstName(),
            registrationDTO.getLastName()
        );
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Вход пользователя", description = "Аутентифицирует пользователя и возвращает токены")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешный вход"),
        @ApiResponse(responseCode = "400", description = "Неверные входные данные"),
        @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequest) {
        TokenResponseDTO tokens = keycloakService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/test")
    public String test(){
        return "test";
    }
} 
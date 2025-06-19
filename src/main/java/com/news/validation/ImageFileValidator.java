package com.news.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class ImageFileValidator implements ConstraintValidator<ImageFile, MultipartFile> {
    private static final byte[] JPEG_SIGNATURE = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final String JPEG_MIME_TYPE = "image/jpeg";

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        // Проверка MIME-типа
        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals(JPEG_MIME_TYPE)) {
            return false;
        }

        // Проверка сигнатуры файла
        try (InputStream inputStream = file.getInputStream()) {
            byte[] header = new byte[3];
            if (inputStream.read(header) != 3) {
                return false;
            }
            return Arrays.equals(header, JPEG_SIGNATURE);
        } catch (IOException e) {
            return false;
        }
    }
} 
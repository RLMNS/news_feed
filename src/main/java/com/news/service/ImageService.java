package com.news.service;

import com.news.model.Image;
import com.news.model.News;
import com.news.repository.ImageRepository;
import com.news.repository.NewsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final NewsRepository newsRepository;
    private static final String JPEG_MIME_TYPE = "image/jpeg";
    private static final String JPEG_EXTENSION = ".jpg";

    @Transactional
    public Long saveImage(MultipartFile file, Long newsId) throws IOException {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NotFoundException(String.format("News with %s not found", newsId)));

        if (!JPEG_MIME_TYPE.equals(file.getContentType())) {
            throw new IllegalArgumentException("Only JPEG images are allowed");
        }

        String fileName = UUID.randomUUID() + JPEG_EXTENSION;

        Image image = new Image();
        image.setFileName(fileName);
        image.setNews(news);
        image.setContentType(JPEG_MIME_TYPE);
        image.setData(file.getBytes());

        Image savedImage = imageRepository.save(image);
        return savedImage.getId();
    }

    @Transactional(readOnly = true)
    public Image getImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Image not found with id: " + id));

        if (!JPEG_MIME_TYPE.equals(image.getContentType())) {
            throw new IllegalStateException("Invalid image format");
        }
        
        return image;
    }

    @Transactional
    public void deleteImage(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new EntityNotFoundException("Image not found with id: " + id);
        }
        imageRepository.deleteById(id);
    }
} 
package com.news.service;

import com.news.dto.NewsCreateDTO;
import com.news.dto.NewsDTO;
import com.news.mapper.NewsMapper;
import com.news.model.News;
import com.news.repository.NewsRepository;
import com.news.repository.ThemeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final ThemeRepository themeRepository;
    private final NewsMapper newsMapper;

    @Transactional(readOnly = true)
    public Page<NewsDTO> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(newsMapper::newsToNewsDto);
    }

    @Transactional(readOnly = true)
    public NewsDTO getNewsById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News not found with id: " + id));
        return newsMapper.newsToNewsDto(news);
    }

    @Transactional
    public NewsDTO createNews(NewsCreateDTO newsDTO) {
        News savedNews = newsRepository.save(newsCreateDtoToNews(newsDTO, new News()));
        return newsMapper.newsToNewsDto(savedNews);
    }

    @Transactional
    public NewsDTO updateNews(Long id, NewsCreateDTO newsDTO) {
        News existingNews = newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News not found with id: " + id));

        News updatedNews = newsRepository.save(newsCreateDtoToNews(newsDTO, existingNews));
        return newsMapper.newsToNewsDto(updatedNews);
    }

    @Transactional
    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new EntityNotFoundException("News not found with id: " + id);
        }
        newsRepository.deleteById(id);
    }

    private News newsCreateDtoToNews(NewsCreateDTO newsDTO, News news) {
        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getContent());
        news.setTheme(themeRepository.getReferenceById(newsDTO.getThemeId()));

        return news;
    }

} 
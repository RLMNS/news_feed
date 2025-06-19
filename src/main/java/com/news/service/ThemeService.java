package com.news.service;

import com.news.dto.NewsDTO;
import com.news.dto.ThemeDTO;
import com.news.mapper.ThemeMapper;
import com.news.model.News;
import com.news.model.Theme;
import com.news.repository.ThemeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final ThemeMapper themeMapper;

    @Transactional
    public ThemeDTO createTheme(String themeName) {
        Theme theme = new Theme();
        theme.setThemeName(themeName);
        return themeMapper.themeToThemeDto(themeRepository.save(theme));
    }

    @Transactional
    public void deleteTheme(Long id) {
        if (!themeRepository.existsById(id)) {
            throw new EntityNotFoundException("Theme not found with id: " + id);
        }
        themeRepository.deleteById(id);
    }

    @Transactional
    public ThemeDTO updateTheme(Long id, String themeName) {
        Theme existingTheme = themeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News not found with id: " + id));
        existingTheme.setThemeName(themeName);
        return themeMapper.themeToThemeDto(themeRepository.save(existingTheme));
    }

    @Transactional(readOnly = true)
    public List<ThemeDTO> getAllThemes() {
        return themeRepository.findAll().stream()
                .map(themeMapper::themeToThemeDto).toList();
    }

    @Transactional(readOnly = true)
    public ThemeDTO getThemeById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News not found with id: " + id));
        return themeMapper.themeToThemeDto(theme);
    }
}

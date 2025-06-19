package com.news.mapper;

import com.news.dto.ThemeDTO;
import com.news.model.Theme;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThemeMapper {
    ThemeDTO themeToThemeDto(Theme theme);
    Theme themeDtoToTheme(ThemeDTO themeDto);
}

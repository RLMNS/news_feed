package com.news.mapper;

import com.news.dto.NewsCreateDTO;
import com.news.dto.NewsDTO;
import com.news.model.Image;
import com.news.model.News;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {CommentMapper.class, ThemeMapper.class})
public interface NewsMapper {
    
    @Mapping(target = "images", source = "images", qualifiedByName = "mapImages")
    @Mapping(target = "comments", source = "comments")
    NewsDTO newsToNewsDto(News news);

    @Mapping(target = "images", ignore = true)
    @Mapping(target = "comments", ignore = true)
    News newsDtoToNews(NewsDTO newsDTO);

    @Named("mapImages")
    default List<Long> mapImages(List<Image> images) {
        if (images == null) {
            return null;
        }
        return images.stream()
                .map(Image::getId)
                .toList();
    }

}

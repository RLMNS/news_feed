package com.news.mapper;

import com.news.dto.CommentDTO;
import com.news.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    CommentDTO commentToCommentDto(Comment comment);
    Comment commentDtoToComment(CommentDTO commentDTO);
}

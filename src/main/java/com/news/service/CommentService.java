package com.news.service;

import com.news.dto.CommentDTO;
import com.news.mapper.CommentMapper;
import com.news.model.Comment;
import com.news.model.News;
import com.news.repository.CommentRepository;
import com.news.repository.NewsRepository;
import com.news.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final NewsRepository newsRepository;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    public Page<CommentDTO> getCommentsByNewsId(Long newsId, Pageable pageable) {
        if (!newsRepository.existsById(newsId)) {
            throw new EntityNotFoundException("News not found with id: " + newsId);
        }
        return commentRepository.findAllByNewsId(newsId, pageable).map(commentMapper::commentToCommentDto);
    }

    @Transactional
    public CommentDTO createComment(Long newsId, String text) {
        News news = newsRepository.findById(newsId).orElseThrow(() ->
                new NotFoundException(String.format("No news with id %s", newsId)));

        Comment comment = new Comment();
        comment.setNews(news);
        comment.setText(text);
        comment.setUsername(SecurityUtil.getCurrentUsername());

        return commentMapper.commentToCommentDto(commentRepository.save(comment));
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));
        String username = SecurityUtil.getCurrentUsername();
        if (!comment.getUsername().equals(username)) {
            throw new AccessDeniedException("Only owner can delete their comments");
        }
        commentRepository.deleteById(id);
    }

    @Transactional
    public Page<CommentDTO> getCommentsByCurrentUser(Pageable pageable) {
        return commentRepository.findAllByUsername(SecurityUtil.getCurrentUsername(), pageable)
                .map(commentMapper::commentToCommentDto);
    }

    @Transactional
    public Page<CommentDTO> getCommentsByUsername(String username, Pageable pageable) {
        return commentRepository.findAllByUsername(username, pageable).map(commentMapper::commentToCommentDto);
    }
}
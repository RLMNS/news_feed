package com.news.repository;

import com.news.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //@Query("SELECT u.id, u.text, u.username, u.createdAt FROM Comment u WHERE u.news.id = ?1")
    Page<Comment> findAllByNewsId(Long newsId, Pageable pageable);
    Page<Comment> findAllByUsername(String currentUsername, Pageable pageable);
    List<Comment> findAllByNewsId(Long id);
}
package com.news.repository;

import com.news.model.News;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    boolean existsById(Long id);
}
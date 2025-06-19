package com.news.model;

import com.news.repository.NewsRepository;
import com.news.repository.ThemeRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class NewsPerformanceTestNoPage {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    public void testNewsPerformance() throws Exception {
        entityManager.createNativeQuery("DELETE FROM news").executeUpdate();
        String insertNewsSql = "INSERT INTO news (id, title, content, created_at, updated_at, theme_id) VALUES (?, ?, ?, ?, ?, ?)";
        List<Object[]> newsBatch = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        for (int i = 10; i <= 110; i++) {
            newsBatch.add(new Object[]{
                    i,
                    "Test News " + i,
                    "Test Content " + i,
                    now,
                    now,
                    1  // theme_id
            });
            now = now.plusYears(1);
        }
        jdbcTemplate.batchUpdate(insertNewsSql, newsBatch);


        String paginationSql =
                "EXPLAIN (ANALYZE, BUFFERS, TIMING, FORMAT JSON) SELECT * FROM news ORDER BY created_at DESC";
        var result = entityManager.createNativeQuery(paginationSql).getResultList();

        for (int i = 0; i < 4; i++) {
            System.out.println(i + "   ------------------------------------------");
            for (Object row : result) {
                System.out.println(row.toString());
            }
            result = entityManager.createNativeQuery(paginationSql).getResultList();
            System.out.println("------------------------------------------");
        }
    }
} 
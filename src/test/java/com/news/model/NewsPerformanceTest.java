package com.news.model;

import com.news.repository.NewsRepository;
import com.news.repository.ThemeRepository;
import jakarta.persistence.TemporalType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class NewsPerformanceTest {

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

        LocalDateTime newNow = now;

        String paginationSql =
                "EXPLAIN (ANALYZE, BUFFERS, TIMING, FORMAT JSON) SELECT * FROM news ORDER BY created_at DESC LIMIT 100";
        String last_page_created_on = now.toString();
        var result = entityManager.createNativeQuery( String.format("""
                EXPLAIN (ANALYZE, BUFFERS, TIMING, FORMAT JSON)
                SELECT * FROM news 
                WHERE created_at <= '%s' 
                ORDER BY created_at DESC LIMIT 100
                """, now)).getResultList();

        for (int i = 0; i < 4; i++) {
            System.out.println(i + "   ------------------------------------------");
            for (Object row : result) {
                System.out.println(row.toString());
            }
            newNow = newNow.minusYears(25);
            result = entityManager.createNativeQuery( String.format("""
                EXPLAIN (ANALYZE, BUFFERS, TIMING, FORMAT JSON)
                SELECT * FROM news 
                WHERE created_at <= '%s' 
                ORDER BY created_at DESC LIMIT 100
                """, newNow)).getResultList();
            System.out.println("------------------------------------------");
        }
    }

    @Test
    public void testPerformance() throws Exception {
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
                    1
            });
            now = now.plusYears(1);
        }
        jdbcTemplate.batchUpdate(insertNewsSql, newsBatch);
    }

    @Test
    public void testPerf() throws Exception {
        String insertNewsSql = "INSERT INTO comments (created_at, news_id, username, text) VALUES (?, ?, ?, ?)";
        List<Object[]> commentsBatch = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int j = 0; j < 10; j++) {
            for (int i = 10; i <= 110; i++) {
                commentsBatch.add(new Object[]{
                        now,
                        i,
                        "user_" + i,
                        "test_"+i
                });
            }
        }
        jdbcTemplate.batchUpdate(insertNewsSql, commentsBatch);
    }
} 
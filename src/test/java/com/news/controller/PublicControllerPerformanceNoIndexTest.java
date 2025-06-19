package com.news.controller;

import com.news.model.News;
import com.news.repository.NewsRepository;
import com.news.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class PublicControllerPerformanceNoIndexTest {

    @Autowired
    private PublicController publicController;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    public void testGetCommentsPerformance() throws Exception {
        entityManager.createNativeQuery("DELETE FROM news").executeUpdate();
        String insertNewsSql = "INSERT INTO news (id, title, content, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        List<Object[]> newsBatch = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 8; i <= 28; i++) {
            newsBatch.add(new Object[]{
                    i,
                    "Test News " + i,
                    "Test Content " + i,
                    now,
                    now
            });
        }
        jdbcTemplate.batchUpdate(insertNewsSql, newsBatch);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\project_news_network\\comments_data.csv")))) {
            reader.readLine();

            String sql = "INSERT INTO comments (text, created_at, username, news_id) VALUES (?, ?, ?, ?)";
            List<Object[]> batch = new ArrayList<>();
            String line;
            int batchSize = 1000;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                batch.add(new Object[]{
                        values[0],
                        LocalDateTime.parse(values[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        values[2],
                        Long.parseLong(values[3])
                });

                if (batch.size() >= batchSize) {
                    jdbcTemplate.batchUpdate(sql, batch);
                    batch.clear();
                }
            }

            if (!batch.isEmpty()) {
                jdbcTemplate.batchUpdate(sql, batch);
            }
        }

        News firstNews = newsRepository.findById(8L).orElseThrow();
        Pageable pageable = PageRequest.of(0, 1000); // Get all 5000 comments in one page

        entityManager.createNativeQuery("DROP INDEX IF EXISTS idx_comments_news_id").executeUpdate();

        long startTimeNoIndex = System.currentTimeMillis();
        var retrievedCommentsNoIndex = publicController.getCommentsByNewsId(firstNews.getId(), pageable);
        long endTimeNoIndex = System.currentTimeMillis();
        long executionTimeNoIndex = endTimeNoIndex - startTimeNoIndex;
        String sql = "EXPLAIN (ANALYZE, BUFFERS, TIMING, FORMAT JSON) SELECT * FROM comments WHERE news_id = 8";
        var result = entityManager.createNativeQuery(sql).getResultList();


        for (int i = 0; i < 10; i++) {
            System.out.println(i + "   ------------------------------------------");
            for (Object row : result){
                System.out.println(row.toString());
            }
            result = entityManager.createNativeQuery(sql).getResultList();
            System.out.println("------------------------------------------");
        }


    }
}
package com.news.dataloader;

import com.news.controller.PublicController;
import com.news.model.News;
import com.news.repository.CommentRepository;
import com.news.repository.NewsRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DataLoader {

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
    //@Transactional
    public void loadData() throws Exception {
        loadThemes();
        loadNews();
        loadComments();
    }

    private void loadThemes() throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream("themes_data.csv")))) {
            reader.readLine();

            String sql = "INSERT INTO themes (created_at, updated_at, theme_name) VALUES (?, ?, ?)";
            List<Object[]> batch = new ArrayList<>();
            String line;
            int batchSize = 10;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                batch.add(new Object[]{
                        LocalDateTime.parse(values[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.parse(values[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        values[2]
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
    }

    private void loadNews() throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream("news_data.csv")))) {
            reader.readLine();

            String sql = "INSERT INTO news (updated_at, created_at, title, content, theme_id) VALUES (?, ?, ?, ?, ?)";
            List<Object[]> batch = new ArrayList<>();
            String line;
            int batchSize = 1000;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                batch.add(new Object[]{
                        LocalDateTime.parse(values[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        LocalDateTime.parse(values[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        values[2],
                        values[3],
                        Long.parseLong(values[4])
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
    }

    private void loadComments() throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream("comments_data.csv")))) {
            reader.readLine();

            String sql = "INSERT INTO comments (text, created_at, username, news_id) VALUES (?, ?, ?, ?)";
            List<Object[]> batch = new ArrayList<>();
            String line;
            int batchSize = 10000;

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
    }
}

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
public class NewsFetchTypeTest {

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
        String paginationSql =
                "EXPLAIN (ANALYZE, BUFFERS, TIMING, FORMAT JSON) select * from comments join public.news t on t.id = comments.news_id;";
        var result = entityManager.createNativeQuery(paginationSql).getResultList();

        for (int i = 0; i < 10; i++) {
            System.out.println(i + "   ------------------------------------------");
            for (Object row : result) {
                System.out.println(row.toString());
            }
            result = entityManager.createNativeQuery(paginationSql).getResultList();
            System.out.println("------------------------------------------");
        }
    }
} 
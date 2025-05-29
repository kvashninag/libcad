package com.example.demo4;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StartupTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupTests.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        for (int i = 0; i < 50; i++) {
            LOGGER.debug("--- BEGIN --- {}", i);
            ResponseEntity<String> responseCreate = restTemplate.postForEntity("/api/v1/image", null, String.class);
            String imageId = responseCreate.getBody();
            LOGGER.info("imageId = {}", imageId);
            ResponseEntity<String> responseJpeg = restTemplate.getForEntity("/api/v1/image/" + imageId + "/jpeg", String.class);
            restTemplate.delete("/api/v1/image/" + imageId);
            LOGGER.info("--- END --- {}", i);
        }
    }
}
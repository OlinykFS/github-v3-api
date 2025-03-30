package com.example.githubv3api;

import com.example.githubv3api.dto.ErrorResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetRepositoriesForNonExistingUser() {
        ResponseEntity<ErrorResponseDto> response = restTemplate.getForEntity("/api/v1/asdasdadasdadas/repositories", ErrorResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getMessage().contains("User not found"));
    }

}

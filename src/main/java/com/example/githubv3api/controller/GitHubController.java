package com.example.githubv3api.controller;


import com.example.githubv3api.dto.RepositoryResponseDto;
import com.example.githubv3api.service.GitHubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class GitHubController {

    private final GitHubService gitHubService;

    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/{username}/repositories")
    public ResponseEntity<List<RepositoryResponseDto>> getUserRepositories(@PathVariable String username) {
        List<RepositoryResponseDto> repositories = gitHubService.getNonForkRepositories(username);
        return ResponseEntity.ok(repositories);
    }
}

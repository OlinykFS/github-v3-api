package com.example.githubv3api.service;

import com.example.githubv3api.dto.BranchDto;
import com.example.githubv3api.dto.RepositoryResponseDto;
import com.example.githubv3api.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {

    private static final String GITHUB_API_BASE = "https://api.github.com/users/";
    private static final String REPOS_SUFFIX = "/repos";
    private static final String BRANCHES_SUFFIX = "/branches";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public List<RepositoryResponseDto> getNonForkRepositories(String username) {
        String reposUrl = GITHUB_API_BASE + username + REPOS_SUFFIX;
        log.info("Fetching repositories from {}", reposUrl);

        try {
            String response = restTemplate.getForObject(reposUrl, String.class);
            return parseRepositories(response, username);
        } catch (HttpClientErrorException.NotFound e) {
            log.error("User not found: {}", username);
            throw new UserNotFoundException("User not found: " + username);
        } catch (Exception e) {
            log.error("Error fetching repositories for {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Error fetching repositories: " + e.getMessage(), e);
        }
    }

    private List<RepositoryResponseDto> parseRepositories(String json, String username) {
        List<RepositoryResponseDto> repositories = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            for (JsonNode repoNode : rootNode) {
                if (repoNode.path("fork").asBoolean(false)) continue;

                String repoName = repoNode.path("name").asText();
                String repoOwner = repoNode.path("owner").path("login").asText();
                List<BranchDto> branches = getBranches(repoOwner, repoName);

                repositories.add(new RepositoryResponseDto(repoName, repoOwner, branches));
            }
        } catch (Exception e) {
            log.error("Error processing repositories for {}: {}", username, e.getMessage(), e);
            throw new RuntimeException("Error processing repositories: " + e.getMessage(), e);
        }
        return repositories;
    }

    private List<BranchDto> getBranches(String owner, String repoName) {
        String branchesUrl = String.format("https://api.github.com/repos/%s/%s%s", owner, repoName, BRANCHES_SUFFIX);
        log.info("Fetching branches from {}", branchesUrl);

        try {
            String response = restTemplate.getForObject(branchesUrl, String.class);
            return parseBranches(response);
        } catch (Exception e) {
            log.error("Error fetching branches for {}/{}: {}", owner, repoName, e.getMessage(), e);
            throw new RuntimeException("Error fetching branches: " + e.getMessage(), e);
        }

    }

    private List<BranchDto> parseBranches(String json) {
        List<BranchDto> branches = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            for (JsonNode branchNode : rootNode) {
                String branchName = branchNode.path("name").asText();
                String lastCommitSha = branchNode.path("commit").path("sha").asText();
                branches.add(new BranchDto(branchName, lastCommitSha));
            }
        } catch (Exception e) {
            log.error("Error processing branches: {}", e.getMessage(), e);
            throw new RuntimeException("Error processing branches: " + e.getMessage(), e);
        }
        return branches;
    }
}

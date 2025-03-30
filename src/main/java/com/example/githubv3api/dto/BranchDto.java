package com.example.githubv3api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BranchDto {
    private String name;
    private String lastCommitSha;
}

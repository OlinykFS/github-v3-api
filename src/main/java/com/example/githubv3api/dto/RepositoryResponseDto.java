package com.example.githubv3api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RepositoryResponseDto {
    private String name;
    private String owner;
    private List<BranchDto> branches;

}

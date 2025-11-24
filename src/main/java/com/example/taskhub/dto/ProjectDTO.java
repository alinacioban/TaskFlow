package com.example.taskhub.dto;

import com.example.taskhub.domain.ProjectPriority;
import com.example.taskhub.domain.ProjectStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectDTO {

    private Long id;
    private String name;
    private String description;
    private String category;

    private LocalDate startDate;
    private LocalDate endDate;

    private ProjectPriority priority;
    private ProjectStatus status;

    private String lead;
    private String techStack;
    private String repository;
}
package com.example.taskhub.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
    this.createdAt = LocalDate.now();
    }

    private String name;

    @Column(length = 1500)
    private String description;

    private String category;     // IT Category
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private ProjectPriority priority;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private String lead;         // Project Leader
    private String techStack;    // Technologies used

    private String repository;   // GitHub / GitLab link
}
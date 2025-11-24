package com.example.taskhub.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskViewDTO {

    private Long id;

    private Long projectId;
    private String projectName;

    private String title;
    private String description;

    private String status;
    private String priority;
    private String type;

    private String assigneeName;

    private Double estimatedHours;
    private Double loggedHours;
    private Double remainingHours;

    // 🔥 câmpuri formate pentru UI
    private String formattedEstimated;
    private String formattedLogged;
    private String formattedRemaining;

    private java.time.LocalDate dueDate;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}

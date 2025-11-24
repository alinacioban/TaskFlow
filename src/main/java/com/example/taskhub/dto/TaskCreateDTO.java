package com.example.taskhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class TaskCreateDTO {

    private Long projectId;

    @NotBlank
    private String title;

    private String description;
    private String priority;
    private String type;

    private Long assigneeId;
    private Long reporterId;

    private String labels;
    private String components;
    private String sprint;

    private Double estimatedHours;
    private Double loggedHours;
    private Double remainingHours;

    private LocalDate dueDate;
}

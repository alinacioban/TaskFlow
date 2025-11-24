package com.example.taskhub.dto;

import com.example.taskhub.domain.Task;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskUpdateDTO {

    private Long id;
    private Long projectId;

    private String title;
    private String description;

    private Double estimatedHours;
    private Double loggedHours;
    private Double remainingHours;

    private String labels;
    private String components;
    private String sprint;

    private String type;
    private String priority;
    private String status;

    private Long assigneeId;
    private Long reporterId;

    private LocalDate dueDate;

    public TaskUpdateDTO(Task t) {
        this.id = t.getId();
        this.projectId = t.getProject().getId();

        this.title = t.getTitle();
        this.description = t.getDescription();

        this.estimatedHours = t.getEstimatedHours();
        this.loggedHours = t.getLoggedHours();
        this.remainingHours = t.getRemainingHours();

        this.labels = t.getLabels();
        this.components = t.getComponents();
        this.sprint = t.getSprint();

        this.type = t.getType().name();
        this.priority = t.getPriority().name();
        this.status = t.getStatus().name();

        this.assigneeId = t.getAssignee() != null ? t.getAssignee().getId() : null;
        this.reporterId = t.getReporter() != null ? t.getReporter().getId() : null;

        this.dueDate = t.getDueDate();
    }
}
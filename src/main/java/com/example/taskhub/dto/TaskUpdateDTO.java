package com.example.taskhub.dto;

import com.example.taskhub.domain.Task;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
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

        if (t == null) {
            return; // 👈 FIX: evităm NullPointerException
        }

        this.id = t.getId();

        if (t.getProject() != null) {
            this.projectId = t.getProject().getId();
        }

        this.title = t.getTitle();
        this.description = t.getDescription();

        this.estimatedHours = t.getEstimatedHours();
        this.loggedHours = t.getLoggedHours();
        this.remainingHours = t.getRemainingHours();

        this.labels = t.getLabels();
        this.components = t.getComponents();
        this.sprint = t.getSprint();

        if (t.getType() != null) this.type = t.getType().name();
        if (t.getPriority() != null) this.priority = t.getPriority().name();
        if (t.getStatus() != null) this.status = t.getStatus().name();

        this.assigneeId = (t.getAssignee() != null) ? t.getAssignee().getId() : null;
        this.reporterId = (t.getReporter() != null) ? t.getReporter().getId() : null;

        this.dueDate = t.getDueDate();
    }
}

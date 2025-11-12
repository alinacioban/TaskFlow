package com.example.taskhub.dto;

import com.example.taskhub.domain.TaskPriority;
import com.example.taskhub.domain.TaskStatus;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;

@Value
@Builder
public class TaskViewDTO {
    Long id;
    Long projectId;
    String projectName;
    String title;
    String description;
    TaskStatus status;
    TaskPriority priority;
    String assigneeName;
    LocalDate dueDate;
    Instant createdAt;
    Instant updatedAt;
}

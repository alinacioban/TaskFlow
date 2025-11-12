package com.example.taskhub.dto;

import com.example.taskhub.domain.Task;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static TaskViewDTO toView(Task task) {
        if (task == null) {
            return null;
        }
        return TaskViewDTO.builder()
                .id(task.getId())
                .projectId(task.getProject() != null ? task.getProject().getId() : null)
                .projectName(task.getProject() != null ? task.getProject().getName() : null)
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .assigneeName(task.getAssignee() != null ? task.getAssignee().getFullName() : null)
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}

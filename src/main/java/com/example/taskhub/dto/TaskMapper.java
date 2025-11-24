package com.example.taskhub.dto;

import com.example.taskhub.domain.Task;

public class TaskMapper {

    public static TaskViewDTO toView(Task t) {
        if (t == null) return null;

        Double est = t.getEstimatedHours();
        Double log = t.getLoggedHours();
        Double rem = t.getRemainingHours();

        return TaskViewDTO.builder()
                .id(t.getId())
                .projectId(t.getProject().getId())
                .projectName(t.getProject().getName())

                .title(t.getTitle())
                .description(t.getDescription())

                // 🔥 aici era eroarea → acum este CORECT
                .status(t.getStatus().name())
                .priority(t.getPriority().name())
                .type(t.getType().name())

                .assigneeName(
                        t.getAssignee() != null ? t.getAssignee().getFullName() : null
                )

                .estimatedHours(est)
                .loggedHours(log)
                .remainingHours(rem)

                .formattedEstimated(formatHours(est))
                .formattedLogged(formatHours(log))
                .formattedRemaining(formatHours(rem))

                .dueDate(t.getDueDate())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())

                .build();
    }

    private static String formatHours(Double h) {
        if (h == null) return "-";

        int total = (int) Math.round(h * 60);
        int hours = total / 60;
        int minutes = total % 60;

        if (minutes == 0) return hours + "h";
        return hours + "h " + minutes + "m";
    }
}

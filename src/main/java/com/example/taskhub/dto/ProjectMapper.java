package com.example.taskhub.dto;

import com.example.taskhub.domain.Project;

public final class ProjectMapper {

    private ProjectMapper() {
    }

    public static ProjectDTO toDto(Project project) {
        if (project == null) {
            return null;
        }
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        return dto;
    }

    public static void updateEntity(Project project, ProjectDTO dto) {
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
    }
}

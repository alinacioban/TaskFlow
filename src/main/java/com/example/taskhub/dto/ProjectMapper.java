package com.example.taskhub.dto;

import com.example.taskhub.domain.Project;

public class ProjectMapper {

    public static ProjectDTO toDto(Project p) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setCategory(p.getCategory());
        dto.setStartDate(p.getStartDate());
        dto.setEndDate(p.getEndDate());
        dto.setPriority(p.getPriority());
        dto.setStatus(p.getStatus());
        dto.setLead(p.getLead());
        dto.setTechStack(p.getTechStack());
        dto.setRepository(p.getRepository());
        return dto;
    }

    public static Project toEntity(ProjectDTO dto) {
        return Project.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .priority(dto.getPriority())
                .status(dto.getStatus())
                .lead(dto.getLead())
                .techStack(dto.getTechStack())
                .repository(dto.getRepository())
                .build();
    }

    public static void updateEntity(Project p, ProjectDTO dto) {
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setCategory(dto.getCategory());
        p.setStartDate(dto.getStartDate());
        p.setEndDate(dto.getEndDate());
        p.setPriority(dto.getPriority());
        p.setStatus(dto.getStatus());
        p.setLead(dto.getLead());
        p.setTechStack(dto.getTechStack());
        p.setRepository(dto.getRepository());
    }
}
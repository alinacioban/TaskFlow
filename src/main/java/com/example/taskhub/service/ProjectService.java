package com.example.taskhub.service;

import com.example.taskhub.domain.Project;
import com.example.taskhub.dto.ProjectDTO;
import com.example.taskhub.dto.ProjectMapper;
import com.example.taskhub.exception.NotFoundException;
import com.example.taskhub.repo.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> listAll() {
        return projectRepository.findAll();
    }

    public List<Project> searchByName(String query) {
        if (query == null || query.isBlank()) {
            return listAll();
        }
        return projectRepository.findByNameContainingIgnoreCase(query);
    }

    public Project getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));
    }

    @Transactional
    public Project create(ProjectDTO dto) {
        Project project = Project.builder().build();
        ProjectMapper.updateEntity(project, dto);
        return projectRepository.save(project);
    }

    @Transactional
    public Project update(Long id, ProjectDTO dto) {
        Project project = getById(id);
        ProjectMapper.updateEntity(project, dto);
        return projectRepository.save(project);
    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new NotFoundException("Project not found");
        }
        projectRepository.deleteById(id);
    }

    public List<Project> findAll() {
    return projectRepository.findAll();
}

}

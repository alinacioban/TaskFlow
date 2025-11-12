package com.example.taskhub.service;

import com.example.taskhub.domain.Project;
import com.example.taskhub.domain.Task;
import com.example.taskhub.domain.TaskPriority;
import com.example.taskhub.domain.TaskStatus;
import com.example.taskhub.dto.TaskCreateDTO;
import com.example.taskhub.exception.ValidationException;
import com.example.taskhub.repo.ProjectRepository;
import com.example.taskhub.repo.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    private Project project;

    @BeforeEach
    void setUp() {
        project = projectRepository.save(Project.builder()
                .name("Test Project")
                .description("Testing")
                .build());
    }

    @Test
    void createTaskShouldPersistWithDefaults() {
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setTitle("Write tests");
        dto.setDescription("Ensure coverage for services");
        dto.setDueDate(LocalDate.now().plusDays(3));

        Task task = taskService.create(project.getId(), dto);

        assertThat(task.getId()).isNotNull();
        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.MEDIUM);
        assertThat(taskRepository.findById(task.getId())).isPresent();
    }

    @Test
    void changeStatusFromDoneToTodoShouldThrow() {
        Task task = taskRepository.save(Task.builder()
                .project(project)
                .title("Finalize release")
                .status(TaskStatus.DONE)
                .priority(TaskPriority.HIGH)
                .build());

        assertThrows(ValidationException.class, () -> taskService.changeStatus(task.getId(), TaskStatus.TODO));
    }
}

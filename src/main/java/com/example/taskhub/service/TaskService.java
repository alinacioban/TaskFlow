package com.example.taskhub.service;

import com.example.taskhub.domain.*;
import com.example.taskhub.dto.TaskCreateDTO;
import com.example.taskhub.dto.TaskMapper;
import com.example.taskhub.dto.TaskUpdateDTO;
import com.example.taskhub.dto.TaskViewDTO;
import com.example.taskhub.exception.NotFoundException;
import com.example.taskhub.exception.ValidationException;
import com.example.taskhub.repo.CommentRepository;
import com.example.taskhub.repo.ProjectRepository;
import com.example.taskhub.repo.TaskRepository;
import com.example.taskhub.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    // ---------------------------
    // GET
    // ---------------------------
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task-ul nu există"));
    }

    // ---------------------------
    // LIST (compatibil 100% cu ProjectController)
    // ---------------------------
    public List<Task> listTasks(
            Long projectId,
            TaskStatus status,
            String assignee,
            String query,
            String sort
    ) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);

        if (query != null && !query.isBlank()) {
            String term = query.toLowerCase(Locale.ROOT);
            tasks = tasks.stream()
                    .filter(t ->
                            (t.getTitle() != null && t.getTitle().toLowerCase().contains(term)) ||
                            (t.getDescription() != null && t.getDescription().toLowerCase().contains(term))
                    )
                    .toList();
        }

        if (status != null) {
            tasks = tasks.stream()
                    .filter(t -> t.getStatus() == status)
                    .toList();
        }

        if (assignee != null && !assignee.isBlank()) {
            String a = assignee.toLowerCase();
            tasks = tasks.stream()
                    .filter(t -> t.getAssignee() != null &&
                            t.getAssignee().getUsername() != null &&
                            t.getAssignee().getUsername().equalsIgnoreCase(a))
                    .toList();
        }

        if (sort != null) {
            switch (sort) {
                case "priority" ->
                        tasks = tasks.stream()
                                .sorted(Comparator.comparing(t -> t.getPriority().ordinal()))
                                .toList();

                case "dueDate" ->
                        tasks = tasks.stream()
                                .sorted(Comparator.comparing(Task::getDueDate,
                                        Comparator.nullsLast(Comparator.naturalOrder())))
                                .toList();

                case "title" ->
                        tasks = tasks.stream()
                                .sorted(Comparator.comparing(Task::getTitle,
                                        Comparator.nullsLast(String::compareToIgnoreCase)))
                                .toList();
            }
        }

        return tasks;
    }

    // ---------------------------
    // CREATE
    // ---------------------------
    @Transactional
    public Task create(Long projectId, TaskCreateDTO dto) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Proiectul nu există"));

        Task t = new Task();
        t.setProject(project);

        t.setTitle(dto.getTitle());
        t.setDescription(dto.getDescription());

        t.setPriority(dto.getPriority() != null ? TaskPriority.valueOf(dto.getPriority()) : TaskPriority.MEDIUM);
        t.setStatus(TaskStatus.TODO);
        t.setType(dto.getType() != null ? TaskType.valueOf(dto.getType()) : TaskType.TASK);

        t.setEstimatedHours(dto.getEstimatedHours());
        t.setLoggedHours(dto.getLoggedHours() != null ? dto.getLoggedHours() : 0.0);
        t.setRemainingHours(dto.getRemainingHours());

        t.setLabels(dto.getLabels());
        t.setComponents(dto.getComponents());
        t.setSprint(dto.getSprint());

        t.setAssignee(resolveUser(dto.getAssigneeId()));
        t.setReporter(resolveUser(dto.getReporterId()));

        t.setDueDate(dto.getDueDate());

        return taskRepository.save(t);
    }

    // ---------------------------
    // UPDATE
    // ---------------------------
    @Transactional
    public Task update(Long id, TaskUpdateDTO dto) {
        Task t = getById(id);

        if (dto.getTitle() != null) t.setTitle(dto.getTitle());
        if (dto.getDescription() != null) t.setDescription(dto.getDescription());

        if (dto.getPriority() != null) t.setPriority(TaskPriority.valueOf(dto.getPriority()));
        if (dto.getStatus() != null) t.setStatus(TaskStatus.valueOf(dto.getStatus()));
        if (dto.getType() != null) t.setType(TaskType.valueOf(dto.getType()));

        if (dto.getEstimatedHours() != null) t.setEstimatedHours(dto.getEstimatedHours());
        if (dto.getLoggedHours() != null) t.setLoggedHours(dto.getLoggedHours());
        if (dto.getRemainingHours() != null) t.setRemainingHours(dto.getRemainingHours());

        t.setAssignee(resolveUser(dto.getAssigneeId()));
        t.setReporter(resolveUser(dto.getReporterId()));

        t.setLabels(dto.getLabels());
        t.setComponents(dto.getComponents());
        t.setSprint(dto.getSprint());

        t.setDueDate(dto.getDueDate());

        return taskRepository.save(t);
    }

    // ---------------------------
    // DELETE
    // ---------------------------
    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new NotFoundException("Task-ul nu există");
        }
        taskRepository.deleteById(id);
    }
    
    // ---------------------------
    // LOG HOURS
    // ---------------------------
    @Transactional
    public void logHours(Long id, double hours) {
        Task t = getById(id);

        double logged = t.getLoggedHours() != null ? t.getLoggedHours() : 0;
        double remaining = t.getRemainingHours() != null ? t.getRemainingHours() : 0;

        t.setLoggedHours(logged + hours);
        t.setRemainingHours(Math.max(remaining - hours, 0));

        taskRepository.save(t);
    }

    // ---------------------------
    // COMMENTS
    // ---------------------------
@Transactional
public Comment addComment(Long taskId, Long authorId, String content) {

    if (content == null || content.isBlank()) {
        throw new ValidationException("Comentariul nu poate fi gol");
    }

    Task task = getById(taskId);
    User author = resolveUser(authorId);

    Comment comment = Comment.builder()
            .task(task)
            .author(author)   // <-- AICI ESTE FIX-UL
            .content(content)
            .build();

    return commentRepository.save(comment);
    }

    public void changeStatus(Long taskId, TaskStatus newStatus) {
    Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new IllegalArgumentException("Task not found"));

    task.setStatus(newStatus);
    taskRepository.save(task);
}

    // ---------------------------
    // Helper
    // ---------------------------
    private User resolveUser(Long id) {
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("User-ul nu există"));
    }

    // ===============================
// LOAD SIDEBAR (pentru controller)
// ===============================
public record SidebarTaskData(
        TaskViewDTO task,
        List<Comment> comments
) {}

public SidebarTaskData loadSidebar(Long taskId) {

    Task t = getById(taskId);

    // convertim task-ul în DTO
    TaskViewDTO dto = TaskMapper.toView(t);

    // luăm comentariile pentru sidebar
    List<Comment> comments = commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId);

    return new SidebarTaskData(dto, comments);
}

}

package com.example.taskhub.service;

import com.example.taskhub.domain.Comment;
import com.example.taskhub.domain.Project;
import com.example.taskhub.domain.Task;
import com.example.taskhub.domain.TaskPriority;
import com.example.taskhub.domain.TaskStatus;
import com.example.taskhub.domain.User;
import com.example.taskhub.dto.TaskCreateDTO;
import com.example.taskhub.dto.TaskUpdateDTO;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public Task getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found"));
        initialize(task);
        return task;
    }

    public List<Task> listTasks(Long projectId, TaskStatus status, String assigneeUsername, String query, String sort) {
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        if (query != null && !query.isBlank()) {
            String term = query.toLowerCase(Locale.ROOT);
            tasks = tasks.stream()
                    .filter(task -> containsIgnoreCase(task.getTitle(), term) || containsIgnoreCase(task.getDescription(), term))
                    .collect(Collectors.toList());
        }
        if (status != null) {
            tasks = tasks.stream()
                    .filter(task -> task.getStatus() == status)
                    .collect(Collectors.toList());
        }
        if (assigneeUsername != null && !assigneeUsername.isBlank()) {
            String usernameLower = assigneeUsername.toLowerCase(Locale.ROOT);
            tasks = tasks.stream()
                    .filter(task -> task.getAssignee() != null && task.getAssignee().getUsername() != null
                            && task.getAssignee().getUsername().toLowerCase(Locale.ROOT).equals(usernameLower))
                    .collect(Collectors.toList());
        }
        tasks = sortTasks(tasks, sort);
        tasks.forEach(this::initialize);
        return tasks;
    }

    private boolean containsIgnoreCase(String source, String term) {
        return source != null && source.toLowerCase(Locale.ROOT).contains(term);
    }

    private List<Task> sortTasks(List<Task> tasks, String sort) {
        if (sort == null || sort.isBlank()) {
            return tasks.stream()
                    .sorted(Comparator.comparing(Task::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        }
        Comparator<Task> comparator;
        switch (sort) {
            case "dueDate" -> comparator = Comparator.comparing(Task::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));
            case "priority" -> comparator = Comparator.comparingInt((Task task) -> task.getPriority() != null ? task.getPriority().ordinal() : TaskPriority.values().length)
                    .thenComparing(Task::getTitle, Comparator.nullsLast(String::compareToIgnoreCase));
            case "title" -> comparator = Comparator.comparing(Task::getTitle, Comparator.nullsLast(String::compareToIgnoreCase));
            default -> comparator = Comparator.comparing(Task::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
        }
        return tasks.stream().sorted(comparator).collect(Collectors.toList());
    }

    @Transactional
    public Task create(Long projectId, TaskCreateDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));
        User assignee = resolveAssignee(dto.getAssigneeId());
        Task task = Task.builder()
                .project(project)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(Objects.requireNonNullElse(dto.getPriority(), TaskPriority.MEDIUM))
                .status(TaskStatus.TODO)
                .assignee(assignee)
                .dueDate(dto.getDueDate())
                .build();
        return taskRepository.save(task);
    }

    @Transactional
    public Task update(Long taskId, TaskUpdateDTO dto) {
        Task task = getById(taskId);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setAssignee(resolveAssignee(dto.getAssigneeId()));
        task.setDueDate(dto.getDueDate());
        return taskRepository.save(task);
    }

    @Transactional
    public void delete(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new NotFoundException("Task not found");
        }
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public Task changeStatus(Long taskId, TaskStatus newStatus) {
        if (newStatus == null) {
            throw new ValidationException("Status is required");
        }
        Task task = getById(taskId);
        TaskStatus current = task.getStatus();
        if (current == TaskStatus.DONE && newStatus != TaskStatus.DONE) {
            throw new ValidationException("Cannot move task out of DONE state");
        }
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    @Transactional
    public Comment addComment(Long taskId, Long authorId, String content) {
        if (content == null || content.isBlank()) {
            throw new ValidationException("Comment content cannot be empty");
        }
        Task task = getById(taskId);
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found"));
        Comment comment = Comment.builder()
                .task(task)
                .author(author)
                .content(content)
                .build();
        return commentRepository.save(comment);
    }

    private User resolveAssignee(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return userRepository.findById(assigneeId)
                .orElseThrow(() -> new ValidationException("Assignee not found"));
    }

    private void initialize(Task task) {
        if (task.getProject() != null) {
            task.getProject().getName();
        }
        if (task.getAssignee() != null) {
            task.getAssignee().getFullName();
        }
    }
}

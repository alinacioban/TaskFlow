package com.example.taskhub.web;

import com.example.taskhub.domain.Project;
import com.example.taskhub.domain.Task;
import com.example.taskhub.domain.TaskPriority;
import com.example.taskhub.domain.TaskStatus;
import com.example.taskhub.dto.TaskCreateDTO;
import com.example.taskhub.dto.TaskMapper;
import com.example.taskhub.dto.TaskUpdateDTO;
import com.example.taskhub.service.ProjectService;
import com.example.taskhub.service.TaskService;
import com.example.taskhub.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;

    @GetMapping("/projects/{projectId}/tasks")
    public String list(@PathVariable Long projectId,
                       @RequestParam(name = "status", required = false) TaskStatus status,
                       @RequestParam(name = "assignee", required = false) String assignee,
                       @RequestParam(name = "q", required = false) String query,
                       @RequestParam(name = "sort", required = false) String sort,
                       Model model) {
        Project project = projectService.getById(projectId);
        model.addAttribute("project", project);
        model.addAttribute("tasks", taskService.listTasks(projectId, status, assignee, query, sort)
                .stream().map(TaskMapper::toView).toList());
        model.addAttribute("status", status);
        model.addAttribute("assignee", assignee);
        model.addAttribute("query", query);
        model.addAttribute("sort", sort);
        model.addAttribute("statuses", TaskStatus.values());
        return "tasks/list";
    }

    @GetMapping("/projects/{projectId}/tasks/new")
    public String createForm(@PathVariable Long projectId, Model model) {
        Project project = projectService.getById(projectId);
        TaskCreateDTO dto = new TaskCreateDTO();
        dto.setPriority(TaskPriority.MEDIUM);
        model.addAttribute("project", project);
        model.addAttribute("taskCreate", dto);
        populateUsers(model);
        model.addAttribute("priorities", TaskPriority.values());
        return "tasks/form";
    }

    @PostMapping("/projects/{projectId}/tasks")
    public String create(@PathVariable Long projectId,
                         @Valid @ModelAttribute("taskCreate") TaskCreateDTO dto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        Project project = projectService.getById(projectId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("project", project);
            populateUsers(model);
            model.addAttribute("priorities", TaskPriority.values());
            return "tasks/form";
        }
        taskService.create(projectId, dto);
        redirectAttributes.addFlashAttribute("success", "Task created successfully");
        return "redirect:/projects/" + projectId;
    }

    @GetMapping("/tasks/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Task task = taskService.getById(id);
        TaskUpdateDTO dto = new TaskUpdateDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setAssigneeId(task.getAssignee() != null ? task.getAssignee().getId() : null);
        dto.setDueDate(task.getDueDate());
        model.addAttribute("taskUpdate", dto);
        model.addAttribute("task", task);
        populateUsers(model);
        model.addAttribute("priorities", TaskPriority.values());
        model.addAttribute("statuses", TaskStatus.values());
        return "tasks/form";
    }

    @PostMapping("/tasks/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("taskUpdate") TaskUpdateDTO dto,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        Task task = taskService.getById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("task", task);
            populateUsers(model);
            model.addAttribute("priorities", TaskPriority.values());
            model.addAttribute("statuses", TaskStatus.values());
            return "tasks/form";
        }
        taskService.update(id, dto);
        redirectAttributes.addFlashAttribute("success", "Task updated successfully");
        return "redirect:/projects/" + task.getProject().getId();
    }

    @PostMapping("/tasks/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Task task = taskService.getById(id);
        Long projectId = task.getProject().getId();
        taskService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Task deleted");
        return "redirect:/projects/" + projectId;
    }

    @PostMapping("/tasks/{id}/move")
    public String move(@PathVariable Long id,
                       @RequestParam("status") TaskStatus status,
                       RedirectAttributes redirectAttributes) {
        Task task = taskService.changeStatus(id, status);
        redirectAttributes.addFlashAttribute("success", "Task status updated");
        return "redirect:/projects/" + task.getProject().getId() + "/board";
    }

    private void populateUsers(Model model) {
        model.addAttribute("users", userService.findAll());
    }
}

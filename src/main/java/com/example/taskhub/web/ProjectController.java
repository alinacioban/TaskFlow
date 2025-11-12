package com.example.taskhub.web;

import com.example.taskhub.domain.Project;
import com.example.taskhub.domain.Task;
import com.example.taskhub.domain.TaskStatus;
import com.example.taskhub.dto.CommentDTO;
import com.example.taskhub.dto.ProjectDTO;
import com.example.taskhub.dto.ProjectMapper;
import com.example.taskhub.dto.TaskMapper;
import com.example.taskhub.service.ProjectService;
import com.example.taskhub.service.CommentService;
import com.example.taskhub.service.TaskService;
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
public class ProjectController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final CommentService commentService;

    @GetMapping("/projects")
    public String list(@RequestParam(name = "q", required = false) String query, Model model) {
        model.addAttribute("projects", projectService.searchByName(query));
        model.addAttribute("query", query);
        return "projects/list";
    }

    @GetMapping("/projects/new")
    public String createForm(Model model) {
        model.addAttribute("projectDTO", new ProjectDTO());
        return "projects/form";
    }

    @PostMapping("/projects")
    public String create(@Valid @ModelAttribute("projectDTO") ProjectDTO projectDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "projects/form";
        }
        Project project = projectService.create(projectDTO);
        redirectAttributes.addFlashAttribute("success", "Project created successfully");
        return "redirect:/projects/" + project.getId();
    }

    @GetMapping("/projects/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Project project = projectService.getById(id);
        model.addAttribute("projectDTO", ProjectMapper.toDto(project));
        return "projects/form";
    }

    @PostMapping("/projects/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("projectDTO") ProjectDTO projectDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "projects/form";
        }
        projectService.update(id, projectDTO);
        redirectAttributes.addFlashAttribute("success", "Project updated successfully");
        return "redirect:/projects/" + id;
    }

    @PostMapping("/projects/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted");
        return "redirect:/projects";
    }

    @GetMapping("/projects/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Project project = projectService.getById(id);
        model.addAttribute("project", project);
        var tasks = taskService.listTasks(id, null, null, null, null);
        model.addAttribute("tasks", tasks.stream().map(TaskMapper::toView).toList());
        model.addAttribute("comments", tasks.stream().collect(java.util.stream.Collectors.toMap(
                Task::getId,
                task -> commentService.listForTask(task.getId()),
                (a, b) -> a,
                java.util.LinkedHashMap::new
        )));
        model.addAttribute("commentDTO", new CommentDTO());
        model.addAttribute("taskStatuses", TaskStatus.values());
        return "projects/detail";
    }

    @GetMapping("/projects/{id}/board")
    public String board(@PathVariable Long id, Model model) {
        Project project = projectService.getById(id);
        model.addAttribute("project", project);
        model.addAttribute("todo", taskService.listTasks(id, TaskStatus.TODO, null, null, "priority")
                .stream().map(TaskMapper::toView).toList());
        model.addAttribute("inProgress", taskService.listTasks(id, TaskStatus.IN_PROGRESS, null, null, "priority")
                .stream().map(TaskMapper::toView).toList());
        model.addAttribute("done", taskService.listTasks(id, TaskStatus.DONE, null, null, "priority")
                .stream().map(TaskMapper::toView).toList());
        return "tasks/kanban";
    }
}

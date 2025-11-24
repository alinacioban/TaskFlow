package com.example.taskhub.web;

import com.example.taskhub.domain.Task;
import com.example.taskhub.domain.TaskPriority;
import com.example.taskhub.domain.TaskStatus;
import com.example.taskhub.domain.TaskType;
import com.example.taskhub.dto.TaskCreateDTO;
import com.example.taskhub.dto.TaskUpdateDTO;
import com.example.taskhub.service.ProjectService;
import com.example.taskhub.service.TaskService;
import com.example.taskhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;

    // CREATE FORM
    @GetMapping("/projects/{projectId}/tasks/new")
    public String createForm(@PathVariable Long projectId, Model model) {

        TaskCreateDTO dto = new TaskCreateDTO();

        model.addAttribute("formObject", dto);
        model.addAttribute("mode", "create");

        model.addAttribute("projectId", projectId);

        model.addAttribute("types", TaskType.values());
        model.addAttribute("priorities", TaskPriority.values());
        model.addAttribute("statuses", TaskStatus.values());

        model.addAttribute("users", userService.findAll());

        return "tasks/form";
    }

    // CREATE POST
    @PostMapping("/projects/{projectId}/tasks")
    public String create(@PathVariable Long projectId,
                         @ModelAttribute("formObject") TaskCreateDTO dto) {

        taskService.create(projectId, dto);
        return "redirect:/projects/" + projectId;
    }

    // EDIT FORM
    @GetMapping("/tasks/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {

        Task t = taskService.getById(id);

        TaskUpdateDTO dto = new TaskUpdateDTO(t);

        model.addAttribute("formObject", dto);
        model.addAttribute("mode", "edit");

        model.addAttribute("taskId", id);
        model.addAttribute("projectId", t.getProject().getId());

        model.addAttribute("types", TaskType.values());
        model.addAttribute("priorities", TaskPriority.values());
        model.addAttribute("statuses", TaskStatus.values());

        model.addAttribute("users", userService.findAll());

        return "tasks/form";
    }

    // UPDATE POST
    @PostMapping("/tasks/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("formObject") TaskUpdateDTO dto) {

        Task t = taskService.update(id, dto);

        return "redirect:/projects/" + t.getProject().getId();
    }

    // DELETE
    @PostMapping("/tasks/{id}/delete")
    public String delete(@PathVariable Long id) {

        Task t = taskService.getById(id);
        Long projectId = t.getProject().getId();

        taskService.delete(id);

        return "redirect:/projects/" + projectId;
    }
    // LOG HOURS
    
    @PostMapping("/tasks/{id}/log")
    @ResponseBody
    public String logHours(@PathVariable Long id, @RequestParam double hours) {
    taskService.logHours(id, hours);
    return "Hours logged!";
    }


}

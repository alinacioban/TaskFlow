package com.example.taskhub.web;

import com.example.taskhub.domain.Task;
import com.example.taskhub.domain.TaskPriority;
import com.example.taskhub.domain.TaskStatus;
import com.example.taskhub.domain.TaskType;
import com.example.taskhub.dto.TaskCreateDTO;
import com.example.taskhub.dto.TaskMapper;
import com.example.taskhub.dto.TaskUpdateDTO;
import com.example.taskhub.dto.TaskViewDTO;
import com.example.taskhub.service.ProjectService;
import com.example.taskhub.service.TaskService;
import com.example.taskhub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;

    // ================
    // CREATE FORM
    // ================
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

    // ================
    // CREATE POST
    // ================
    @PostMapping("/projects/{projectId}/tasks")
    public String create(@PathVariable Long projectId,
                         @ModelAttribute("formObject") TaskCreateDTO dto) {

        taskService.create(projectId, dto);
        return "redirect:/projects/" + projectId;
    }

    // ================
    // EDIT FORM
    // ================
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

    // ================
    // UPDATE POST
    // ================
    @PostMapping("/tasks/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("formObject") TaskUpdateDTO dto) {

        Task t = taskService.update(id, dto);

        return "redirect:/projects/" + t.getProject().getId();
    }

    // ================
    // DELETE
    // ================
    @PostMapping("/tasks/{id}/delete")
    public String delete(@PathVariable Long id) {

        Task t = taskService.getById(id);
        Long projectId = t.getProject().getId();

        taskService.delete(id);

        return "redirect:/projects/" + projectId;
    }


    // ================================================================
    // GLOBAL TASK LIST (simplu, tabular) — dacă vrei să îl folosești
    // ================================================================
    @GetMapping("/tasks")
public String listAll(Model model) {

    List<Task> tasks = taskService.findAll();

    List<TaskViewDTO> dtos = tasks.stream()
            .map(TaskMapper::toView)
            .toList();

    model.addAttribute("tasks", dtos);

    return "tasks/global-list";  // <-- versiunea în română
}


    // ================================================================
    // GLOBAL KANBAN ( /kanban ) — Noul modul corect implementat
    // ================================================================
   // =======================
//   GLOBAL KANBAN
// =======================
@GetMapping("/kanban")
public String globalKanban(
        @RequestParam(required = false) Long projectId,
        @RequestParam(required = false) Long assigneeId,
        @RequestParam(required = false) TaskPriority priority,
        @RequestParam(required = false) TaskStatus status,
        Model model
) {
    // 1. Filtrare taskuri
    List<Task> filtered = taskService.filterTasks(projectId, assigneeId, priority, status);
    List<TaskViewDTO> dtos = filtered.stream()
            .map(TaskMapper::toView)
            .toList();

    // 2. Grupare
    model.addAttribute("todo", dtos.stream()
            .filter(t -> t.getStatus().equals("TODO"))
            .toList());

    model.addAttribute("inprogress", dtos.stream()
            .filter(t -> t.getStatus().equals("IN_PROGRESS"))
            .toList());

    model.addAttribute("done", dtos.stream()
            .filter(t -> t.getStatus().equals("DONE"))
            .toList());

    // 3. Filtre dropdown
    model.addAttribute("projects", projectService.findAll());
    model.addAttribute("users", userService.findAll());
    model.addAttribute("priorities", TaskPriority.values());
    model.addAttribute("statuses", TaskStatus.values());

    // 4. Trimitem înapoi filtrele selectate
    model.addAttribute("selectedProject", projectId);
    model.addAttribute("selectedAssignee", assigneeId);
    model.addAttribute("selectedPriority", priority);
    model.addAttribute("selectedStatus", status);

    return "tasks/global-kanban";
}


@PostMapping("/tasks/{id}/status")
@ResponseBody
public String updateStatusFromKanban(
        @PathVariable Long id,
        @RequestParam("value") String newStatus
) {
    taskService.changeStatus(id, TaskStatus.valueOf(newStatus));
    return "OK";
}


}

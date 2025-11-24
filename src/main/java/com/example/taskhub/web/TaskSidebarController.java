package com.example.taskhub.web;

import com.example.taskhub.domain.TaskStatus;
import com.example.taskhub.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tasks/{id}/sidebar")
public class TaskSidebarController {

    private final TaskService taskService;

    // ---------------------------------------------------------
    // SIDEBAR VIEW
    // ---------------------------------------------------------
    @GetMapping
    public String sidebar(@PathVariable Long id, Model model) {

        var data = taskService.loadSidebar(id);

        model.addAttribute("task", data.task());
        model.addAttribute("comments", data.comments());

        return "tasks/sidebar :: sidebarContent";
    }

    // ---------------------------------------------------------
    // LOG HOURS — no conflict with TaskController anymore
    // ---------------------------------------------------------
    @PostMapping("/log")
    @ResponseBody
    public String logHours(
            @PathVariable Long id,
            @RequestParam("hours") double hours
    ) {
        taskService.logHours(id, hours);
        return "OK";
    }

    // ---------------------------------------------------------
    // CHANGE STATUS
    // ---------------------------------------------------------
    @PostMapping("/status")
    @ResponseBody
    public String changeStatus(
            @PathVariable Long id,
            @RequestParam("value") String status
    ) {
        taskService.changeStatus(id, TaskStatus.valueOf(status));
        return "OK";
    }

    // ---------------------------------------------------------
    // UPDATE DUE DATE
    // ---------------------------------------------------------
    @PostMapping("/duedate")
    @ResponseBody
    public String updateDueDate(
            @PathVariable Long id,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        taskService.updateDueDate(id, date);
        return "OK";
    }

    // ---------------------------------------------------------
    // ADD COMMENT
    // ---------------------------------------------------------
    @PostMapping("/comment")
    @ResponseBody
    public String addComment(
            @PathVariable Long id,
            @RequestParam("content") String content
    ) {
        taskService.addComment(id, 1L, content); // TODO: user logat
        return "OK";
    }
}

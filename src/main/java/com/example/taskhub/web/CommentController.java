package com.example.taskhub.web;

import com.example.taskhub.domain.Comment;
import com.example.taskhub.domain.Task;
import com.example.taskhub.dto.CommentDTO;
import com.example.taskhub.service.CommentService;
import com.example.taskhub.service.TaskService;
import com.example.taskhub.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final TaskService taskService;
    private final UserService userService;

    @PostMapping("/tasks/{taskId}/comments")
    public String addComment(@PathVariable Long taskId,
                             @Valid @ModelAttribute("commentDTO") CommentDTO dto,
                             BindingResult bindingResult,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        Task task = taskService.getById(taskId);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Comment content cannot be empty");
            return "redirect:/projects/" + task.getProject().getId();
        }
        var user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
        taskService.addComment(taskId, user.getId(), dto.getContent());
        redirectAttributes.addFlashAttribute("success", "Comment added");
        return "redirect:/projects/" + task.getProject().getId();
    }

    @PostMapping("/comments/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Comment comment = commentService.getById(id);
        Long projectId = comment.getTask().getProject().getId();
        commentService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Comment deleted");
        return "redirect:/projects/" + projectId;
    }
}

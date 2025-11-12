package com.example.taskhub.service;

import com.example.taskhub.domain.Comment;
import com.example.taskhub.exception.NotFoundException;
import com.example.taskhub.repo.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> listForTask(Long taskId) {
        return commentRepository.findByTaskIdOrderByCreatedAtAsc(taskId);
    }

    public Comment getById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));
    }

    @Transactional
    public void delete(Long commentId) {
        Comment comment = getById(commentId);
        commentRepository.delete(comment);
    }
}

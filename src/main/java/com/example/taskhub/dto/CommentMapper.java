package com.example.taskhub.dto;

import com.example.taskhub.domain.Comment;

public final class CommentMapper {

    private CommentMapper() {
    }

    public static CommentDTO toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDTO dto = new CommentDTO();
        dto.setTaskId(comment.getTask() != null ? comment.getTask().getId() : null);
        dto.setContent(comment.getContent());
        return dto;
    }
}

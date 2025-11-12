package com.example.taskhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDTO {

    private Long taskId;

    @NotBlank
    @Size(max = 1000)
    private String content;
}

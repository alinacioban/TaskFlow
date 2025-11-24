package com.example.taskhub.domain;

public enum TaskStatus {
    TODO,               // De făcut
    IN_PROGRESS,        // În desfășurare
    IN_REVIEW,          // În review / Code review
    BLOCKED,            // Blocat
    TESTING,            // În testare
    READY_FOR_DEPLOY,   // Pregătit de deploy
    DONE                // Finalizat
}

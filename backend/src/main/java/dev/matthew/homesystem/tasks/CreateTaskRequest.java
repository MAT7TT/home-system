package dev.matthew.homesystem.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank
        @Size(max = 200)
        String title,
        String notes,
        Long categoryId,
        String plannedDate,
        String scheduledStart,
        String scheduledEnd,
        String dueAt
) {
}

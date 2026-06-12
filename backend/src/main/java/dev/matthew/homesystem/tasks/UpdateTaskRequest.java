package dev.matthew.homesystem.tasks;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateTaskRequest(
        @NotBlank
        @Size(max = 200)
        String title,
        String notes,
        Long categoryId,
        String plannedDate,
        String scheduledStart,
        String scheduledEnd,
        String dueAt,
        List<Long> tagIds,
        List<Long> routineIds
) {
}

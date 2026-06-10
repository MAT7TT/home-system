package dev.matthew.homesystem.tasks;

public record Task(
        Long id,
        String title,
        String notes,
        String status,
        Long categoryId,
        String plannedDate,
        String scheduledStart,
        String scheduledEnd,
        String dueAt,
        String completedAt,
        String skippedAt,
        String skipReason,
        String createdAt,
        String updatedAt
) {
}

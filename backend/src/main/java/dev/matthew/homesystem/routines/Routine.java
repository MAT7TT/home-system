package dev.matthew.homesystem.routines;

public record Routine(
        Long id,
        String name,
        String notes,
        Long categoryId,
        String routineType,
        double targetAmount,
        String targetUnit,
        String period,
        String startDate,
        String endDate,
        boolean active,
        String createdAt,
        String updatedAt
) {
}
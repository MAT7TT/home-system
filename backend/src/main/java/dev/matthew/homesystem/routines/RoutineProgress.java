package dev.matthew.homesystem.routines;

public record RoutineProgress(
        Long routineId,
        String name,
        String routineType,
        double targetAmount,
        double completedAmount,
        String targetUnit,
        String period
) {
}
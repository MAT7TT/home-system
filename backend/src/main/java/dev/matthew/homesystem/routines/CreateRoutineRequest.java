package dev.matthew.homesystem.routines;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateRoutineRequest(
        @NotBlank
        @Size(max = 200)
        String name,

        String notes,

        Long categoryId,

        @NotBlank
        String routineType,

        @Positive
        double targetAmount,

        @NotBlank
        @Size(max = 50)
        String targetUnit,

        @NotBlank
        String period,

        @NotBlank
        String startDate,

        String endDate
) {
}
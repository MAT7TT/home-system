package dev.matthew.homesystem.tasks;

import jakarta.validation.constraints.Size;

public record SkipTaskRequest(
        @Size(max = 500)
        String reason
) {
}

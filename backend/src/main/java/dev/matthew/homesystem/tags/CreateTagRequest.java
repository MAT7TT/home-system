package dev.matthew.homesystem.tags;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTagRequest(
        @NotBlank
        @Size(max = 80)
        String name,

        @Size(max = 32)
        String color
) {
}

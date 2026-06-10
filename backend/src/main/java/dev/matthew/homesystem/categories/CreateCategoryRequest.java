package dev.matthew.homesystem.categories;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank
        @Size(max = 80)
        String name,

        @Size(max = 32)
        String color
) {
}

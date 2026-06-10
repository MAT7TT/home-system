package dev.matthew.homesystem.categories;

public record Category(
        Long id,
        String name,
        String color,
        int sortOrder,
        boolean active,
        String createdAt,
        String updatedAt
) {
}
/**
 * Why a record?
 *
 * Because this class is just data. It does not need setters, getters, or behaviour yet. Java automatically gives you:
 *
 * Constructor
 * Accessor methods
 * Equals
 * hashCode
 * toString
 */
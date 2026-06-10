package dev.matthew.homesystem.categories;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    private final JdbcClient jdbcClient;

    public CategoryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Category> findAll() {
        return jdbcClient.sql("""
                    SELECT id, name, color, sort_order, active, created_at, updated_at
                    FROM categories
                    WHERE active = 1
                    ORDER BY sort_order, name
                    """)
            .query((rs, rowNum) -> new Category(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("sort_order"),
                    rs.getInt("active") == 1,
                    rs.getString("created_at"),
                    rs.getString("updated_at")
        )).list();
    }

    public Category create(CreateCategoryRequest request) {
        return jdbcClient.sql("""
                INSERT INTO categories (name, color)
                VALUES (:name, :color)
                RETURNING id, name, color, sort_order, active, created_at, updated_at
                """)
            .param("name", request.name().trim())
            .param("color", request.color())
            .query((rs, rowNum) -> new Category(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("sort_order"),
                    rs.getInt("active") == 1,
                    rs.getString("created_at"),
                    rs.getString("updated_at")
            ))
            .single();
    }
}

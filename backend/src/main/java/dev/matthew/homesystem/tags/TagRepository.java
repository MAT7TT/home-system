package dev.matthew.homesystem.tags;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepository {

    private final JdbcClient jdbcClient;

    public TagRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Tag> findAll() {
        return jdbcClient.sql("""
                        SELECT id, name, color, created_at, updated_at
                        FROM tags
                        ORDER BY name
                        """)
                .query((rs, rowNum) -> new Tag(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("color"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .list();
    }

    public Tag create(CreateTagRequest request) {
        return jdbcClient.sql("""
                        INSERT INTO tags (name, color)
                        VALUES (:name, :color)
                        RETURNING id, name, color, created_at, updated_at
                        """)
                .param("name", request.name().trim())
                .param("color", request.color())
                .query((rs, rowNum) -> new Tag(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("color"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .single();
    }
}
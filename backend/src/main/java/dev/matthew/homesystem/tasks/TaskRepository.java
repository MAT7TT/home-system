package dev.matthew.homesystem.tasks;

import dev.matthew.homesystem.routines.Routine;
import dev.matthew.homesystem.tags.Tag;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepository {

    private final JdbcClient jdbcClient;

    public TaskRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Task> findAll() {
        return jdbcClient.sql("""
                        SELECT id, title, notes, status, category_id, planned_date,
                               scheduled_start, scheduled_end, due_at, completed_at,
                               skipped_at, skip_reason, created_at, updated_at
                        FROM tasks
                        ORDER BY
                            CASE status
                                WHEN 'active' THEN 0
                                WHEN 'done' THEN 1
                                WHEN 'skipped' THEN 2
                                WHEN 'cancelled' THEN 3
                                ELSE 4
                            END,
                            planned_date IS NULL,
                            planned_date,
                            created_at
                        """)
                .query((rs, rowNum) -> new Task(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getString("status"),
                        getNullableLong(rs, "category_id"),
                        rs.getString("planned_date"),
                        rs.getString("scheduled_start"),
                        rs.getString("scheduled_end"),
                        rs.getString("due_at"),
                        rs.getString("completed_at"),
                        rs.getString("skipped_at"),
                        rs.getString("skip_reason"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .list();
    }

    public Optional<Task> findById(Long id) {
        return jdbcClient.sql("""
                    SELECT id, title, notes, status, category_id, planned_date,
                           scheduled_start, scheduled_end, due_at, completed_at,
                           skipped_at, skip_reason, created_at, updated_at
                    FROM tasks
                    WHERE id = :id
                    """)
                .param("id", id)
                .query((rs, rowNum) -> new Task(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getString("status"),
                        getNullableLong(rs, "category_id"),
                        rs.getString("planned_date"),
                        rs.getString("scheduled_start"),
                        rs.getString("scheduled_end"),
                        rs.getString("due_at"),
                        rs.getString("completed_at"),
                        rs.getString("skipped_at"),
                        rs.getString("skip_reason"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .optional();
    }

    public List<Task> findActive() {
        return jdbcClient.sql("""
                    SELECT id, title, notes, status, category_id, planned_date,
                           scheduled_start, scheduled_end, due_at, completed_at,
                           skipped_at, skip_reason, created_at, updated_at
                    FROM tasks
                        WHERE status = 'active'
                          AND (
                              planned_date IS NULL
                              OR planned_date != date('now')
                          )
                          AND (
                              scheduled_start IS NULL
                              OR date(scheduled_start) != date('now')
                          )
                    ORDER BY
                        planned_date IS NULL,
                        planned_date,
                        scheduled_start IS NULL,
                        scheduled_start,
                        created_at
                    """)
                .query((rs, rowNum) -> new Task(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getString("status"),
                        getNullableLong(rs, "category_id"),
                        rs.getString("planned_date"),
                        rs.getString("scheduled_start"),
                        rs.getString("scheduled_end"),
                        rs.getString("due_at"),
                        rs.getString("completed_at"),
                        rs.getString("skipped_at"),
                        rs.getString("skip_reason"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .list();
    }

    public List<Task> findToday() {
        return jdbcClient.sql("""
                    SELECT id, title, notes, status, category_id, planned_date,
                           scheduled_start, scheduled_end, due_at, completed_at,
                           skipped_at, skip_reason, created_at, updated_at
                    FROM tasks
                    WHERE planned_date = date('now') 
                    OR date(scheduled_start) = date('now')
                    ORDER BY
                        scheduled_start IS NULL,
                        scheduled_start,
                        created_at
                    """)
                .query((rs, rowNum) -> new Task(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getString("status"),
                        getNullableLong(rs, "category_id"),
                        rs.getString("planned_date"),
                        rs.getString("scheduled_start"),
                        rs.getString("scheduled_end"),
                        rs.getString("due_at"),
                        rs.getString("completed_at"),
                        rs.getString("skipped_at"),
                        rs.getString("skip_reason"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .list();
    }

    public Task create(CreateTaskRequest request) {
        return jdbcClient.sql("""
                        INSERT INTO tasks (
                            title, notes, category_id, planned_date,
                            scheduled_start, scheduled_end, due_at
                        )
                        VALUES (
                            :title, :notes, :categoryId, :plannedDate,
                            :scheduledStart, :scheduledEnd, :dueAt
                        )
                        RETURNING id, title, notes, status, category_id, planned_date,
                                  scheduled_start, scheduled_end, due_at, completed_at,
                                  skipped_at, skip_reason, created_at, updated_at
                        """)
                .param("title", request.title().trim())
                .param("notes", request.notes())
                .param("categoryId", request.categoryId())
                .param("plannedDate", request.plannedDate())
                .param("scheduledStart", request.scheduledStart())
                .param("scheduledEnd", request.scheduledEnd())
                .param("dueAt", request.dueAt())
                .query((rs, rowNum) -> new Task(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getString("status"),
                        getNullableLong(rs, "category_id"),
                        rs.getString("planned_date"),
                        rs.getString("scheduled_start"),
                        rs.getString("scheduled_end"),
                        rs.getString("due_at"),
                        rs.getString("completed_at"),
                        rs.getString("skipped_at"),
                        rs.getString("skip_reason"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .single();
    }

    public Task update(Long id, UpdateTaskRequest request) {
        return jdbcClient.sql("""
                    UPDATE tasks
                    SET title = :title,
                        notes = :notes,
                        category_id = :categoryId,
                        planned_date = :plannedDate,
                        scheduled_start = :scheduledStart,
                        scheduled_end = :scheduledEnd,
                        due_at = :dueAt,
                        updated_at = CURRENT_TIMESTAMP
                    WHERE id = :id
                    RETURNING id, title, notes, status, category_id, planned_date,
                              scheduled_start, scheduled_end, due_at, completed_at,
                              skipped_at, skip_reason, created_at, updated_at
                    """)
                .param("id", id)
                .param("title", request.title().trim())
                .param("notes", request.notes())
                .param("categoryId", request.categoryId())
                .param("plannedDate", request.plannedDate())
                .param("scheduledStart", request.scheduledStart())
                .param("scheduledEnd", request.scheduledEnd())
                .param("dueAt", request.dueAt())
                .query((rs, rowNum) -> new Task(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getString("status"),
                        getNullableLong(rs, "category_id"),
                        rs.getString("planned_date"),
                        rs.getString("scheduled_start"),
                        rs.getString("scheduled_end"),
                        rs.getString("due_at"),
                        rs.getString("completed_at"),
                        rs.getString("skipped_at"),
                        rs.getString("skip_reason"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .single();
    }

    public Task complete(Long id) {
        return jdbcClient.sql("""
                    UPDATE tasks
                    SET status = 'done',
                        completed_at = CURRENT_TIMESTAMP,
                        skipped_at = NULL,
                        skip_reason = NULL,
                        updated_at = CURRENT_TIMESTAMP
                    WHERE id = :id
                    RETURNING id, title, notes, status, category_id, planned_date,
                              scheduled_start, scheduled_end, due_at, completed_at,
                              skipped_at, skip_reason, created_at, updated_at
                    """)
                .param("id", id)
                .query((rs, rowNum) -> new Task(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getString("status"),
                        getNullableLong(rs, "category_id"),
                        rs.getString("planned_date"),
                        rs.getString("scheduled_start"),
                        rs.getString("scheduled_end"),
                        rs.getString("due_at"),
                        rs.getString("completed_at"),
                        rs.getString("skipped_at"),
                        rs.getString("skip_reason"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .single();
    }

    public Task skip(Long id, SkipTaskRequest request) {
        return jdbcClient.sql("""
                    UPDATE tasks
                    SET status = 'skipped',
                        skipped_at = CURRENT_TIMESTAMP,
                        skip_reason = :skipReason,
                        completed_at = NULL,
                        updated_at = CURRENT_TIMESTAMP
                    WHERE id = :id
                    RETURNING id, title, notes, status, category_id, planned_date,
                              scheduled_start, scheduled_end, due_at, completed_at,
                              skipped_at, skip_reason, created_at, updated_at
                    """)
                .param("id", id)
                .param("skipReason", request.reason())
                .query((rs, rowNum) -> new Task(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("notes"),
                        rs.getString("status"),
                        getNullableLong(rs, "category_id"),
                        rs.getString("planned_date"),
                        rs.getString("scheduled_start"),
                        rs.getString("scheduled_end"),
                        rs.getString("due_at"),
                        rs.getString("completed_at"),
                        rs.getString("skipped_at"),
                        rs.getString("skip_reason"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .single();
    }

    public void linkRoutine(Long taskId, Long routineId) {
        jdbcClient.sql("""
                    INSERT OR IGNORE INTO task_routines (task_id, routine_id)
                    VALUES (:taskId, :routineId)
                    """)
                .param("taskId", taskId)
                .param("routineId", routineId)
                .update();
    }

    public void unlinkRoutine(Long taskId, Long routineId) {
        jdbcClient.sql("""
                    DELETE FROM task_routines
                    WHERE task_id = :taskId
                      AND routine_id = :routineId
                    """)
                .param("taskId", taskId)
                .param("routineId", routineId)
                .update();
    }

    public List<Routine> findRoutinesForTask(Long taskId) {
        return jdbcClient.sql("""
                    SELECT r.id, r.name, r.notes, r.category_id, r.routine_type,
                           r.target_amount, r.target_unit, r.period, r.start_date,
                           r.end_date, r.active, r.created_at, r.updated_at
                    FROM routines r
                    INNER JOIN task_routines tr ON tr.routine_id = r.id
                    WHERE tr.task_id = :taskId
                    ORDER BY r.name
                    """)
                .param("taskId", taskId)
                .query((rs, rowNum) -> new Routine(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("notes"),
                        getNullableLong(rs, "category_id"),
                        rs.getString("routine_type"),
                        rs.getDouble("target_amount"),
                        rs.getString("target_unit"),
                        rs.getString("period"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getInt("active") == 1,
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .list();
    }

    public void linkTag(Long taskId, Long tagId) {
        jdbcClient.sql("""
                    INSERT OR IGNORE INTO task_tags (task_id, tag_id)
                    VALUES (:taskId, :tagId)
                    """)
                .param("taskId", taskId)
                .param("tagId", tagId)
                .update();
    }

    public void unlinkTag(Long taskId, Long tagId) {
        jdbcClient.sql("""
                    DELETE FROM task_tags
                    WHERE task_id = :taskId
                      AND tag_id = :tagId
                    """)
                .param("taskId", taskId)
                .param("tagId", tagId)
                .update();
    }

    public List<Tag> findTagsForTask(Long taskId) {
        return jdbcClient.sql("""
                    SELECT t.id, t.name, t.color, t.created_at, t.updated_at
                    FROM tags t
                    INNER JOIN task_tags tt ON tt.tag_id = t.id
                    WHERE tt.task_id = :taskId
                    ORDER BY t.name
                    """)
                .param("taskId", taskId)
                .query((rs, rowNum) -> new Tag(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("color"),
                        rs.getString("created_at"),
                        rs.getString("updated_at")
                ))
                .list();
    }

    private Long getNullableLong(java.sql.ResultSet rs, String columnName) throws java.sql.SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }
}
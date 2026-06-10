package dev.matthew.homesystem.routines;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoutineRepository {

    private final JdbcClient jdbcClient;

    public RoutineRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Routine> findAll() {
        return jdbcClient.sql("""
                        SELECT id, name, notes, category_id, routine_type, target_amount,
                               target_unit, period, start_date, end_date, active,
                               created_at, updated_at
                        FROM routines
                        ORDER BY active DESC, name
                        """)
                .query((rs, rowNum) -> mapRoutine(rs))
                .list();
    }

    public Optional<Routine> findById(Long id) {
        return jdbcClient.sql("""
                        SELECT id, name, notes, category_id, routine_type, target_amount,
                               target_unit, period, start_date, end_date, active,
                               created_at, updated_at
                        FROM routines
                        WHERE id = :id
                        """)
                .param("id", id)
                .query((rs, rowNum) -> mapRoutine(rs))
                .optional();
    }

    public RoutineProgress getProgress(Long id) {
        return jdbcClient.sql("""
                    SELECT r.id,
                           r.name,
                           r.routine_type,
                           r.target_amount,
                           r.target_unit,
                           r.period,
                           COUNT(t.id) AS completed_amount
                    FROM routines r
                    LEFT JOIN task_routines tr ON tr.routine_id = r.id
                    LEFT JOIN tasks t ON t.id = tr.task_id
                        AND t.status = 'done'
                        AND (
                            (r.routine_type = 'daily_check'
                                AND date(t.completed_at) = date('now'))
                            OR
                            (r.routine_type = 'weekly_quota'
                                AND date(t.completed_at) >= date('now', 'weekday 1', '-7 days')
                                AND date(t.completed_at) < date('now', 'weekday 1'))
                            OR
                            (r.routine_type = 'monthly_quota'
                                AND strftime('%Y-%m', t.completed_at) = strftime('%Y-%m', 'now'))
                            OR
                            (r.routine_type = 'cumulative_goal'
                                AND date(t.completed_at) >= date(r.start_date))
                        )
                    WHERE r.id = :id
                    GROUP BY r.id, r.name, r.routine_type, r.target_amount, r.target_unit, r.period
                    """)
                .param("id", id)
                .query((rs, rowNum) -> new RoutineProgress(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("routine_type"),
                        rs.getDouble("target_amount"),
                        rs.getDouble("completed_amount"),
                        rs.getString("target_unit"),
                        rs.getString("period")
                ))
                .single();
    }

    public Routine create(CreateRoutineRequest request) {
        return jdbcClient.sql("""
                        INSERT INTO routines (
                            name, notes, category_id, routine_type, target_amount,
                            target_unit, period, start_date, end_date
                        )
                        VALUES (
                            :name, :notes, :categoryId, :routineType, :targetAmount,
                            :targetUnit, :period, :startDate, :endDate
                        )
                        RETURNING id, name, notes, category_id, routine_type, target_amount,
                                  target_unit, period, start_date, end_date, active,
                                  created_at, updated_at
                        """)
                .param("name", request.name().trim())
                .param("notes", request.notes())
                .param("categoryId", request.categoryId())
                .param("routineType", request.routineType())
                .param("targetAmount", request.targetAmount())
                .param("targetUnit", request.targetUnit())
                .param("period", request.period())
                .param("startDate", request.startDate())
                .param("endDate", request.endDate())
                .query((rs, rowNum) -> mapRoutine(rs))
                .single();
    }

    public Routine update(Long id, UpdateRoutineRequest request) {
        return jdbcClient.sql("""
                        UPDATE routines
                        SET name = :name,
                            notes = :notes,
                            category_id = :categoryId,
                            routine_type = :routineType,
                            target_amount = :targetAmount,
                            target_unit = :targetUnit,
                            period = :period,
                            start_date = :startDate,
                            end_date = :endDate,
                            active = :active,
                            updated_at = CURRENT_TIMESTAMP
                        WHERE id = :id
                        RETURNING id, name, notes, category_id, routine_type, target_amount,
                                  target_unit, period, start_date, end_date, active,
                                  created_at, updated_at
                        """)
                .param("id", id)
                .param("name", request.name().trim())
                .param("notes", request.notes())
                .param("categoryId", request.categoryId())
                .param("routineType", request.routineType())
                .param("targetAmount", request.targetAmount())
                .param("targetUnit", request.targetUnit())
                .param("period", request.period())
                .param("startDate", request.startDate())
                .param("endDate", request.endDate())
                .param("active", request.active() ? 1 : 0)
                .query((rs, rowNum) -> mapRoutine(rs))
                .single();
    }

    private Routine mapRoutine(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new Routine(
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
        );
    }

    private Long getNullableLong(java.sql.ResultSet rs, String columnName) throws java.sql.SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }
}
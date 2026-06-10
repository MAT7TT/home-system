CREATE TABLE categories (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL UNIQUE,
                            color TEXT,
                            sort_order INTEGER NOT NULL DEFAULT 0,
                            active INTEGER NOT NULL DEFAULT 1,
                            created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       title TEXT NOT NULL,
                       notes TEXT,
                       status TEXT NOT NULL DEFAULT 'active',
                       category_id INTEGER,
                       planned_date TEXT,
                       scheduled_start TEXT,
                       scheduled_end TEXT,
                       due_at TEXT,
                       completed_at TEXT,
                       skipped_at TEXT,
                       skip_reason TEXT,
                       created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE routines (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          name TEXT NOT NULL,
                          notes TEXT,
                          routine_type TEXT NOT NULL,
                          target_amount REAL NOT NULL DEFAULT 1,
                          target_unit TEXT NOT NULL DEFAULT 'count',
                          category_id INTEGER,
                          period TEXT NOT NULL,
                          start_date TEXT NOT NULL,
                          end_date TEXT,
                          active INTEGER NOT NULL DEFAULT 1,
                          created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

CREATE TABLE task_routines (
                               task_id INTEGER NOT NULL,
                               routine_id INTEGER NOT NULL,
                               PRIMARY KEY (task_id, routine_id),
                               FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
                               FOREIGN KEY (routine_id) REFERENCES routines(id) ON DELETE CASCADE
);

CREATE INDEX idx_categories_active ON categories(active);
CREATE INDEX idx_tasks_category_id ON tasks(category_id);

CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_planned_date ON tasks(planned_date);
CREATE INDEX idx_tasks_scheduled_start ON tasks(scheduled_start);
CREATE INDEX idx_tasks_completed_at ON tasks(completed_at);

CREATE INDEX idx_routines_active ON routines(active);
CREATE INDEX idx_task_routines_task_id ON task_routines(task_id);
CREATE INDEX idx_task_routines_routine_id ON task_routines(routine_id);
CREATE INDEX idx_routines_category_id ON routines(category_id);
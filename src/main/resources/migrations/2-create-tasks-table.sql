CREATE TABLE tasks (
  task_id      SERIAL PRIMARY KEY,
  message      TEXT,
  is_completed BOOLEAN DEFAULT FALSE
);
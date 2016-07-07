CREATE TABLE users_tasks (
  user_task_id SERIAL PRIMARY KEY,
  user_id      INT REFERENCES users (user_id) ON DELETE CASCADE,
  task_id      INT REFERENCES tasks (task_id) ON DELETE CASCADE,
  UNIQUE (user_id, task_id)
);
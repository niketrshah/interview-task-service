CREATE TABLE users (
  user_id SERIAL PRIMARY KEY,
  name    TEXT
);

INSERT INTO users (name)
VALUES
  ('Ross'),
  ('Chandler'),
  ('Joey'),
  ('Monica'),
  ('Rachel'),
  ('Phoebe');
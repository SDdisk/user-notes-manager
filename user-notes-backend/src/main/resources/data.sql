insert into user_table(id, username, email, password, roles)
values ('550e8400-e29b-41d4-a716-446655440000', 'user', 'user@mail.com', '$2a$10$5EtCK8Vr1O8KH3ZyNgSLdefwIvJ1PgzKQ5oUOPzwkXrnrE3KrXkC6', ARRAY['USER']),
      ('550e8400-e29b-41d4-a716-446655440001', 'admin', 'admin@mail.com', '$2a$10$5hycYOdyde/9hZtB/i0/k.FFifj1DopvLrHZ5mb3aj/7ZEaSgZTBm', ARRAY['ADMIN', 'USER']);
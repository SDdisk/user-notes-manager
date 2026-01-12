insert into user_table(id, username, email, password, roles)
values ('550e8400-e29b-41d4-a716-446655440000', 'user', 'user@mail.com', '$2a$10$hmeBSV.B7I06r8FhWOPwN.6JNIk7ZnbihqOkb3qCituMSJaWqtSFK', ARRAY['USER']),
      ('550e8400-e29b-41d4-a716-446655440001', 'admin', 'admin@mail.com', '$2a$10$50e3syPci07I6zoMfAj5KORQ.GeACbEPgvV7BB3Y87miQFuJnwE.K', ARRAY['ADMIN', 'USER']);
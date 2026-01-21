CREATE TABLE user_table (
    user_id UUID PRIMARY KEY NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(255)[] NOT NULL
);

CREATE TABLE note_table (
    note_id UUID PRIMARY KEY NOT NULL,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    pinned BOOLEAN NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_notes_user FOREIGN KEY (user_id)
        REFERENCES user_table(user_id)
        ON DELETE CASCADE
);
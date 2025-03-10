CREATE TYPE USER_NAME as (
    first_name ONLY_LETTERS,
    second_name ONLY_LETTERS,
    last_name ONLY_LETTERS
);

CREATE TABLE users (
   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   name USER_NAME NOT NULL,
   email EMAIL NOT NULL UNIQUE,
   created_at DATE NOT NULL,
   updated_at DATE NOT NULL
);

CREATE INDEX idx_users_email ON users (email);
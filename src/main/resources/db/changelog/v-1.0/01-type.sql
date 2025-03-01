CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE DOMAIN EMAIL AS VARCHAR
    CHECK (
        VALUE ~ '^[\w\-\.]+@([\w-]+\.)+[\w-]{2,4}$'
    );

CREATE DOMAIN ONLY_LETTERS AS VARCHAR
    CHECK (
        VALUE ~ '^[a-zA-Zа-яА-Я]+$'
    );



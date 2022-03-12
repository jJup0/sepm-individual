CREATE TABLE IF NOT EXISTS horse
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255) NULL,
    birthdate       DATE NOT NULL,
    sex             VARCHAR(6) NOT NULL CHECK (sex in ('male', 'female')),
    owner           VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS parents(
    child         BIGINT PRIMARY KEY FOREIGN REFERENCES horses(id),
    mother        BIGINT PRIMARY KEY FOREIGN REFERENCES horses(id),
    father        BIGINT PRIMARY KEY FOREIGN REFERENCES horses(id)
);
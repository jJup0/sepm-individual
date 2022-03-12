CREATE TABLE IF NOT EXISTS horse
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255) NULL,
    birthdate       DATE NOT NULL,
    sex             VARCHAR(6) NOT NULL CHECK (sex in ('male', 'female')),
    owner           VARCHAR(255) NULL
    mother        BIGINT NULL FOREIGN REFERENCES horses(id),
    father        BIGINT NULL FOREIGN REFERENCES horses(id)
);
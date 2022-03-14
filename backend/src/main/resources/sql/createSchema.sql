CREATE TYPE IF NOT EXISTS HORSESEX AS ENUM ('male', 'female');

CREATE TABLE IF NOT EXISTS horse
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255) NULL,
    birthdate       DATE NOT NULL,
    sex             HORSESEX NOT NULL,
    owner           VARCHAR(255) NULL,
    mother          BIGINT NULL,
    father          BIGINT NULL,
    FOREIGN KEY (mother) REFERENCES horse(id) ON DELETE CASCADE,
    FOREIGN KEY (father) REFERENCES horse(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS owner
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstName       VARCHAR(255) NOT NULL,
    lastName        VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NULL
);

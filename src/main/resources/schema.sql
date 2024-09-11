-- first to execute
CREATE TABLE IF NOT EXISTS trainees (
    id BIGSERIAL PRIMARY KEY,          -- Primary Key
    first_name VARCHAR(255) NOT NULL,  -- Inherited from User
    last_name VARCHAR(255) NOT NULL,   -- Inherited from User
    username VARCHAR(255) NOT NULL UNIQUE,  -- Inherited from User
    password VARCHAR(255) NOT NULL,    -- Inherited from User
    is_active BOOLEAN NOT NULL,        -- Inherited from User
    date_of_birth DATE,
    address VARCHAR(255)
);

-- third to execute
CREATE TABLE IF NOT EXISTS trainers (
    id BIGSERIAL PRIMARY KEY,          -- Primary Key
    first_name VARCHAR(255) NOT NULL,  -- Inherited from User
    last_name VARCHAR(255) NOT NULL,   -- Inherited from User
    username VARCHAR(255) NOT NULL UNIQUE,  -- Inherited from User
    password VARCHAR(255) NOT NULL,    -- Inherited from User
    is_active BOOLEAN NOT NULL,        -- Inherited from User
    specialization_id BIGINT REFERENCES training_types(id)
);

-- second to execute
CREATE TABLE IF NOT EXISTS training_types (
    id BIGSERIAL PRIMARY KEY,
    training_type_name VARCHAR(255) NOT NULL UNIQUE
);

-- last to execute
CREATE TABLE IF NOT EXISTS trainings (
    id BIGSERIAL PRIMARY KEY,
    trainee_id BIGINT NOT NULL REFERENCES trainees(id),  -- FK to Trainee
    trainer_id BIGINT NOT NULL REFERENCES trainers(id),  -- FK to Trainer
    training_name VARCHAR(255) NOT NULL,
    training_type_id BIGINT REFERENCES training_types(id),
    training_date DATE NOT NULL,
    training_duration INTEGER NOT NULL
);

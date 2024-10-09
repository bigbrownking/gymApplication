INSERT INTO trainees (first_name, last_name, username, password, is_active, date_of_birth, address)
VALUES
('Alice', 'Johnson', 'alice_stage', 'password1', TRUE, '1990-05-10', '123 Stage Street'),
('Bob', 'Smith', 'bob_stage', 'password2', TRUE, '1985-09-21', '456 Stage Avenue'),
('Charlie', 'Brown', 'charlie_stage', 'password3', TRUE, '1992-12-15', '789 Stage Blvd');

INSERT INTO trainers (first_name, last_name, username, password, is_active, specialization_id)
VALUES
('David', 'Jones', 'david_stage', 'password4', TRUE, 1),
('Eve', 'Williams', 'eve_stage', 'password5', TRUE, 2),
('Frank', 'Thompson', 'frank_stage', 'password6', TRUE, 3);

INSERT INTO training_types (training_type_name)
VALUES
('Strength Training'),
('Cardio'),
('Yoga');

INSERT INTO trainings (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES
(1, 1, 'Morning Strength Session', 1, '2024-10-01', 60),
(2, 2, 'Afternoon Cardio Blast', 2, '2024-10-02', 45),
(3, 3, 'Evening Yoga Flow', 3, '2024-10-03', 30),
(1, 2, 'Mixed Cardio & Strength', 1, '2024-10-04', 50),
(3, 1, 'Strength Endurance', 1, '2024-10-05', 70);

-- first to execute
INSERT INTO training_types (training_type_name) VALUES
('Strength'),
('Yoga'),
('Pilates');

-- second to execute
INSERT INTO trainees (first_name, last_name, username, password, is_active, date_of_birth, address) VALUES
('John', 'Doe', 'john.doe', 'password123', TRUE, '1990-01-15', '123 Elm Street'),
('Jane', 'Smith', 'jane.smith', 'password456', TRUE, '1992-05-22', '456 Oak Avenue'),
('Robert', 'Johnson', 'robert.johnson', 'password789', TRUE, '1988-09-30', '789 Pine Road'),
('Emily', 'Davis', 'emily.davis', 'password000', TRUE, '1995-12-10', '101 Maple Lane'),
('Michael', 'Brown', 'michael.brown', 'password111', TRUE, '1993-07-04', '202 Birch Boulevard');


-- third to execute
INSERT INTO trainers (first_name, last_name, username, password, is_active, specialization_id) VALUES
('Alice', 'Green', 'alice.green', 'password123', TRUE, 1),
('David', 'White', 'david.white', 'password456', TRUE, 2),
('Sophia', 'Black', 'sophia.black', 'password789', TRUE, 3),
('Olivia', 'Moore', 'olivia.moore', 'password111', TRUE, 1),
('James', 'Wilson', 'james.wilson', 'password000', TRUE, 2);


-- last to execute
INSERT INTO trainings (trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration) VALUES
(1, 2, 'Morning Cardio', 1, '2024-09-15', 60),
(1, 1, 'Strength Training Session', 2, '2024-09-17', 45),
(2, 3, 'Yoga Basics', 3, '2024-09-16', 90),
(2, 5, 'Advanced Pilates',3, '2024-09-18', 75),
(3, 4, 'Cardio and Strength', 1, '2024-09-19', 60),
(3, 5, 'Yoga Intermediate', 2, '2024-09-20', 90),
(4, 2, 'Strength and Conditioning', 2, '2024-09-21', 60),
(4, 3, 'Pilates for Flexibility', 3, '2024-09-22', 75),
(5, 1, 'Cardio Routine', 1, '2024-09-23', 45),
(5, 2, 'Strength Training and Yoga', 2, '2024-09-24', 60);


-- first to execute
INSERT INTO training_types (id, training_type_name) VALUES
(1, 'Strength'),
(2, 'Yoga'),
(3, 'Pilates');

-- second to execute
INSERT INTO trainees (id, first_name, last_name, username, password, is_active, date_of_birth, address) VALUES
(1, 'John', 'Doe', 'john.doe', '$2a$12$JygSradZed5xLLF2rvBYoutbprm9KixWQ5PiOzgK.iVTk815uUTRm', TRUE, '1990-01-15', '123 Elm Street'),
(2, 'Jane', 'Smith', 'jane.smith', '$2a$12$GaK4i.b/AeykAuG1EmppSui914Gg0ObW0VVljBQ009Shna4hbu2.S', TRUE, '1992-05-22', '456 Oak Avenue'),
(3, 'Robert', 'Johnson', 'robert.johnson', '$2a$12$Zxyi8KUq3fykl7m4MZyaoeiHk8liJzO.IcA6g3Fno1Z5qh2a8w426', TRUE, '1988-09-30', '789 Pine Road'),
(4, 'Emily', 'Davis', 'emily.davis', '$2a$12$Al.sr3gQpVp1QmBbj4aNYO88p71HrFSJR75Fp.60KM9j5Mey.Revq', TRUE, '1995-12-10', '101 Maple Lane'),
(5, 'Michael', 'Brown', 'michael.brown', '$2a$12$VLs.pHvartM7DHaQNX2eqe3pjnQ2avQ4Svc2TdmmPdGeKpg2zhzPq', TRUE, '1993-07-04', '202 Birch Boulevard');


-- third to execute
INSERT INTO trainers (id, first_name, last_name, username, password, is_active, specialization_id) VALUES
(1, 'Alice', 'Green', 'alice.green', '$2a$12$2U44YwRzuPBZoxArxJre3u4OBZoZSqa1IqubCv3wL8J8EZH1sMO1q', TRUE, 1),
(2, 'David', 'White', 'david.white', '$2a$12$XUYkkq9EmY0kKDtHIlEe7O0pNRdcpyASyu8Wc401J8H1Pxd6lx4EG', TRUE, 2),
(3, 'Sophia', 'Black', 'sophia.black', '$2a$12$z3tQQ94U5IL1S02fj0ZCYuLeI1XEBWCpLMZ.g3q.CMRTGFtnJ1RYW', TRUE, 3),
(4, 'Olivia', 'Moore', 'olivia.moore', '$2a$12$c56cRUL3CX/l/PlOgHjp5OJnFcwOdSj2Ak1w3ZaitMXEnFzDWZg72', TRUE, 1),
(5, 'James', 'Wilson', 'james.wilson', '$2a$12$ul4mATURDIzmBvWseJiFcu6KUiMYlrpo4Xowc07h3wgX0.ENFCNA2', TRUE, 2);


-- last to execute
INSERT INTO trainings (id, trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration) VALUES
(1, 1, 2, 'Morning Cardio', 1, '2024-09-15', 60),
(2, 1, 1, 'Strength Training Session', 2, '2024-09-17', 45),
(3, 2, 3, 'Yoga Basics', 3, '2024-09-16', 90),
(4, 2, 5, 'Advanced Pilates',3, '2024-09-18', 75),
(5, 3, 4, 'Cardio and Strength', 1, '2024-09-19', 60),
(6, 3, 5, 'Yoga Intermediate', 2, '2024-09-20', 90),
(7, 4, 2, 'Strength and Conditioning', 2, '2024-09-21', 60),
(8, 4, 3, 'Pilates for Flexibility', 3, '2024-09-22', 75),
(9, 5, 1, 'Cardio Routine', 1, '2024-09-23', 45),
(10, 5, 2, 'Strength Training and Yoga', 2, '2024-09-24', 60);


package org.example.service;

import org.example.models.Trainee;

import java.util.List;

public interface TraineeService {
    void createTrainee(Trainee trainee);

    void updateTrainee(Trainee trainee);

    void deleteTrainee(Long userId);

    Trainee getTraineeByUsername(String username);

    List<Trainee> getAllTrainee();
}

package org.example.service;

import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.Date;
import java.util.List;

public interface TraineeService {
    void createTrainee(Trainee trainee);

    void updateTrainee(Trainee trainee);

    void deleteTrainee(String username);

    Trainee getTraineeByUsername(String username);
    Trainee getTraineeByUsernameAndPassword(String username, String password);
    List<Trainee> getAllTrainee();
    void changePassword(Trainee trainee, String password);

    void activateTrainee(String username);

    void deactivateTrainee(String username);
    List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String trainerName, TrainingTypeEntity trainingType);

    List<Trainer> getTrainersNotAssignedToTrainee(String username);
}

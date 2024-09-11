package org.example.service;

import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.Date;
import java.util.List;

public interface TrainerService {
    void createTrainer(Trainer trainer);
    void updateTrainer(Trainer trainer);
    Trainer getTrainerByUsername(String username);
    Trainer getTrainerByUsernameAndPassword(String username, String password);
    List<Trainer> getAllTrainers();
    void changePassword(Trainer trainer, String password);
    void activateTrainer(String username);
    void deactivateTrainer(String username);
    List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String traineeName);
}

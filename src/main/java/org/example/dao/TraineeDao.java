package org.example.dao;

import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    void create(Trainee trainee);
    void update(Trainee trainee);
    boolean delete(String username);
    List<Trainee> listAll();
    Optional<Trainee> findByUsername(String username);
    Optional<Trainee> findByUsernameAndPassword(String username, String password);
    List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate,  String trainerName, TrainingTypeEntity trainingType);
    List<Trainer> getTrainersNotAssignedToTrainee(String username);
}

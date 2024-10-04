package org.example.dao;

import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    void create(Trainer trainer);
    void update(Trainer trainer);
    Optional<Trainer> findByUsername(String username) throws Exception;
    List<Trainer> listAll() throws Exception;
    Optional<Trainer> findByUsernameAndPassword(String username, String password) throws Exception;
    List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String traineeName) throws Exception;
    boolean isSpecializationValid(TrainingTypeEntity trainingTypeEntity);
    List<Trainee> allTraineesOfTrainer(String username) throws Exception;
}

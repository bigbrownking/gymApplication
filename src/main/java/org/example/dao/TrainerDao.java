package org.example.dao;

import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    boolean create(Trainer trainer);
    void update(Trainer trainer);
    Optional<Trainer> findByUsername(String username);
    List<Trainer> listAll();
    Optional<Trainer> findByUsernameAndPassword(String username, String password);
    List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String traineeName);
    boolean isSpecializationValid(TrainingTypeEntity trainingTypeEntity);
}

package org.example.dao;

import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.InvalidDataException;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainerDao {
    void create(Trainer trainer) throws InvalidDataException;
    void update(Trainer trainer) throws EntityNotFoundException, InvalidDataException;
    Optional<Trainer> findByUsername(String username) throws EntityNotFoundException;
    List<Trainer> listAll() throws InvalidDataException;
    List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String traineeName);
    boolean isSpecializationValid(TrainingTypeEntity trainingTypeEntity);
    List<Trainee> allTraineesOfTrainer(String username) throws EntityNotFoundException;
}

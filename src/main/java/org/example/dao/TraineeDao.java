package org.example.dao;

import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.InvalidDataException;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TraineeDao {
    void create(Trainee trainee) throws InvalidDataException;
    void update(Trainee trainee) throws EntityNotFoundException, InvalidDataException;
    boolean delete(String username) throws EntityNotFoundException, InvalidDataException;
    List<Trainee> listAll() throws InvalidDataException;
    Optional<Trainee> findByUsername(String username) throws EntityNotFoundException;
    List<Training> getTrainingByCriteria(String username, LocalDateTime startDate, LocalDateTime endDate, String trainerName, TrainingTypeEntity trainingType);
    List<Trainer> getTrainersNotAssignedToTrainee(String username) throws EntityNotFoundException, InvalidDataException;
    List<Trainer> getTrainersAssignedToTrainee(String username) throws EntityNotFoundException, InvalidDataException;
}

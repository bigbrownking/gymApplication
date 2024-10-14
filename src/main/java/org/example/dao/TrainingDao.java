package org.example.dao;

import org.example.exceptions.InvalidDataException;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.List;

public interface TrainingDao {
    void create(Training training) throws InvalidDataException;
    List<TrainingTypeEntity> getTrainingTypes() throws InvalidDataException;

}

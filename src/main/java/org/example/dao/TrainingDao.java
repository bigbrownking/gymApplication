package org.example.dao;

import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.List;

public interface TrainingDao {
    void create(Training training);
    Training select(Long trainingId);
    List<Training> listAll();
    void updateTraining(Training training);
    TrainingTypeEntity getTrainingType(String trainingType);

}

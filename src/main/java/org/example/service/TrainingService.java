package org.example.service;

import org.example.models.Training;

import java.util.List;

public interface TrainingService {
    void createTraining(Training training);

    public Training getTraining(Long trainingId);
    List<Training> getAllTrainings();
    void updateTraining(Training training);
}

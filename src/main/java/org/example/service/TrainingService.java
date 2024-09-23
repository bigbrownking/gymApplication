package org.example.service;

import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.dto.responses.training.GetTrainingTypesResponseDto;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.List;

public interface TrainingService {
    void createTraining(CreateTrainingRequestDto createTrainingRequestDto);
    List<Training> getAllTrainings();
    GetTrainingTypesResponseDto getTrainingTypes();
}
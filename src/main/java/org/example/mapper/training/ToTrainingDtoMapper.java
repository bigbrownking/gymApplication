package org.example.mapper.training;

import org.example.dto.responses.training.GetTrainingTypesResponseDto;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.List;

public interface ToTrainingDtoMapper {
    GetTrainingTypesResponseDto toGetTrainingTypesDto(List<TrainingTypeEntity> trainingTypeEntities);
}

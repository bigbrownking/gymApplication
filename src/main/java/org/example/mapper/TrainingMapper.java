package org.example.mapper;

import org.example.dto.TrainingDto;
import org.example.mapper.training.ToTrainingDtoMapper;
import org.example.mapper.training.ToTrainingMapper;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.List;

public interface TrainingMapper extends ToTrainingMapper, ToTrainingDtoMapper {
    List<TrainingDto> convertTrainingsToDto(List<Training> trainings);
}

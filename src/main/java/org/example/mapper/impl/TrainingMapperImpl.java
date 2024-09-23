package org.example.mapper.impl;


import org.example.dto.TrainingDto;
import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.dto.responses.training.GetTrainingTypesResponseDto;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingMapperImpl implements TrainingMapper {

    @Override
    public GetTrainingTypesResponseDto toGetTrainingTypesDto(List<TrainingTypeEntity> trainingTypeEntities) {
        GetTrainingTypesResponseDto dto = new GetTrainingTypesResponseDto();
        dto.setTrainingTypeEntities(trainingTypeEntities);

        return dto;
    }

    @Override
    public Training toTraining(CreateTrainingRequestDto createTrainingRequestDto, Trainee trainee, Trainer trainer) {
        if (createTrainingRequestDto == null) {
            return null;
        }

        Training training = new Training();
        training.setTrainingName(createTrainingRequestDto.getTrainingName());
        training.setTrainingDate(createTrainingRequestDto.getTrainingDate());
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingDuration(createTrainingRequestDto.getDuration());

        return training;
    }

    @Override
    public List<TrainingDto> convertTrainingsToDto(List<Training> trainings) {
        if (trainings == null) {
            return null;
        }

        List<TrainingDto> dtos = new ArrayList<>();
        for (Training training : trainings) {
            TrainingDto dto = new TrainingDto();
            dto.setTrainingName(training.getTrainingName());
            dto.setTrainingDate(training.getTrainingDate());
            dto.setTrainingType(training.getTrainingType());
            dto.setTraineeName(training.getTrainee().getUsername());
            dto.setDuration(training.getTrainingDuration());

            dtos.add(dto);
        }
        return dtos;
    }
}

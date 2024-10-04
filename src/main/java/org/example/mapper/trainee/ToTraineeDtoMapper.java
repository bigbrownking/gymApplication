package org.example.mapper.trainee;

import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.responses.trainee.*;
import org.example.models.Trainee;

import java.util.List;


public interface ToTraineeDtoMapper {
    CreateTraineeResponseDto toCreateTraineeDto(Trainee trainee);
    GetNotAssignedTrainersResponseDto toGetNotAssignedTrainersDto(List<TrainerDto> trainerDtos);
    GetTraineeByUsernameResponseDto toGetTraineeByUsernameDto(Trainee trainee, List<TrainerDto> trainerDtos);
    GetTraineeTrainingListResponseDto toGetTraineeTrainingListDto(List<TrainingDto> trainingDtos);
    UpdateTraineeResponseDto toUpdateTraineeDto(Trainee trainee, List<TrainerDto> trainerDtos);
}

package org.example.mapper.trainer;

import org.example.dto.TraineeDto;
import org.example.dto.TrainingDto;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.dto.responses.trainer.UpdateTrainerResponseDto;
import org.example.models.Trainer;

import java.util.List;

public interface ToTrainerDtoMapper {
    CreateTrainerResponseDto toCreateTrainerDto(Trainer trainer);
    GetTrainerByUsernameResponseDto toGetTrainerByUsernameDto(Trainer trainer, List<TraineeDto> traineeDtos);
    GetTrainerTrainingListResponseDto toGetTrainingListDto(List<TrainingDto> trainingDtos);
    UpdateTrainerResponseDto toUpdateTrainerDto(Trainer trainer, List<TraineeDto> traineeDtos);
}

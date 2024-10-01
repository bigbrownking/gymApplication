package org.example.mapper.training;

import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;

public interface ToTrainingMapper {
    Training toTraining(CreateTrainingRequestDto createTrainingRequestDto, Trainee trainee, Trainer trainer);
}

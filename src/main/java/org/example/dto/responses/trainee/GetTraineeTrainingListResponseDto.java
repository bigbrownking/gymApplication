package org.example.dto.responses.trainee;

import lombok.Getter;
import lombok.Setter;
import org.example.dto.TrainingDto;
import org.example.models.Training;

import java.util.List;

@Getter
@Setter
public class GetTraineeTrainingListResponseDto {

    private List<TrainingDto> trainingDtos;
}

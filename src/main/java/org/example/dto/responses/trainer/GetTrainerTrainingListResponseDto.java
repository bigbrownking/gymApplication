package org.example.dto.responses.trainer;

import lombok.Getter;
import lombok.Setter;
import org.example.dto.TrainingDto;

import java.util.List;

@Getter
@Setter
public class GetTrainerTrainingListResponseDto {
    private List<TrainingDto> trainingDtos;
}

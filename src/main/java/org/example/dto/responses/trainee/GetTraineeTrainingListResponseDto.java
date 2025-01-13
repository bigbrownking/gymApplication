package org.example.dto.responses.trainee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.TrainingDto;
import org.example.models.Training;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTraineeTrainingListResponseDto {

    private List<TrainingDto> trainingDtos;
}

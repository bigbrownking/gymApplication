package org.example.dto.responses.trainer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.TrainingDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTrainerTrainingListResponseDto {
    private List<TrainingDto> trainingDtos;
}

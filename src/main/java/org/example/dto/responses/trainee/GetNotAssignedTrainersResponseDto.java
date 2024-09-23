package org.example.dto.responses.trainee;

import lombok.Getter;
import lombok.Setter;
import org.example.dto.TrainerDto;

import java.util.List;

@Getter
@Setter
public class GetNotAssignedTrainersResponseDto {

    private List<TrainerDto> trainerDtos;

}

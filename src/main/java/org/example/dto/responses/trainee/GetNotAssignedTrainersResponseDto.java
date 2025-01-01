package org.example.dto.responses.trainee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.TrainerDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetNotAssignedTrainersResponseDto {

    private List<TrainerDto> trainerDtos;

}

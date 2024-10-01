package org.example.dto.responses.trainer;

import lombok.Getter;
import lombok.Setter;
import org.example.dto.TraineeDto;
import org.example.models.TrainingTypeEntity;

import java.util.List;

@Getter
@Setter
public class GetTrainerByUsernameResponseDto {

    private String firstname;
    private String lastname;
    private TrainingTypeEntity specialization;
    private boolean isActive;

    private List<TraineeDto> traineeDtos;
}

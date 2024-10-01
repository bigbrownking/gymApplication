package org.example.dto.responses.trainer;

import lombok.Getter;
import lombok.Setter;
import org.example.dto.TraineeDto;
import org.example.models.TrainingTypeEntity;

import java.util.List;


@Getter
@Setter
public class UpdateTrainerResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeEntity specialization;
    private boolean isActive;

    private List<TraineeDto> traineeDtos;

}

package org.example.dto.responses.trainer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.TraineeDto;
import org.example.models.TrainingTypeEntity;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeEntity specialization;
    private boolean isActive;

    private List<TraineeDto> traineeDtos;

}

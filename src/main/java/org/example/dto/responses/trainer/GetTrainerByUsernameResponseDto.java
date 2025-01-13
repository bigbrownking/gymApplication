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
public class GetTrainerByUsernameResponseDto {

    private String firstname;
    private String lastname;
    private TrainingTypeEntity specialization;
    private boolean isActive;

    private List<TraineeDto> traineeDtos;
}

package org.example.dto.requests.training;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class CreateTrainingRequestDto {

    @NotNull(message = "Trainee username is required")
    private String traineeUsername;

    @NotNull(message = "Trainer username is required")
    private String trainerUsername;

    @NotNull(message = "Training name is required")
    private String trainingName;

    @NotNull(message = "Training date is required")
    private Date trainingDate;

    @NotNull(message = "Duration is required")
    private Integer duration;
}

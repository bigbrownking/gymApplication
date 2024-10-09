package org.example.dto.requests.trainer;

import lombok.Getter;
import lombok.Setter;
import org.example.models.TrainingTypeEntity;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class CreateTrainerRequestDto {

    @NotNull(message = "Firstname is required")
    private String firstName;

    @NotNull(message = "Lastname is required")
    private String lastName;

    @NotNull(message = "Specialization is required")
    private TrainingTypeEntity specialization;
}

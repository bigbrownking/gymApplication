package org.example.dto.requests.trainer;

import lombok.Getter;
import lombok.Setter;
import org.example.models.TrainingTypeEntity;
import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotNull;


@Getter
@Setter
public class UpdateTrainerRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Firstname is required")
    private String firstName;

    @NotNull(message = "Lastname is required")
    private String lastName;

    private TrainingTypeEntity specialization;

    @NotNull(message = "IsActive is required")
    private boolean isActive;
}

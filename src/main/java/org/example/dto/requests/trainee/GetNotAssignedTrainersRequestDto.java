package org.example.dto.requests.trainee;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class GetNotAssignedTrainersRequestDto {

    @NotNull(message = "Username is required")
    private String username;
}

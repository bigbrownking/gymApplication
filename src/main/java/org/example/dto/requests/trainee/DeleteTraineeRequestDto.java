package org.example.dto.requests.trainee;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DeleteTraineeRequestDto {

    @NotNull(message = "Username is required")
    private String username;
}

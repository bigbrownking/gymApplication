package org.example.dto.requests.trainee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetNotAssignedTrainersRequestDto {

    @NotNull(message = "Username is required")
    private String username;
}

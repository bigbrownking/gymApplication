package org.example.dto.requests.trainer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GetTrainerByUsernameRequestDto {

    @NotNull(message = "Username is required")
    private String username;
}
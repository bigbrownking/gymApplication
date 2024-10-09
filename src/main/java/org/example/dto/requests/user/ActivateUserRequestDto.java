package org.example.dto.requests.user;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class ActivateUserRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "IsActive is required")
    private boolean isActive;
}

package org.example.dto.requests.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeactivateUserRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "IsActive is required")
    private boolean isActive;
}

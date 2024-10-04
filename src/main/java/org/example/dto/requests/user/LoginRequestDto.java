package org.example.dto.requests.user;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class LoginRequestDto{

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Password is required")
    private String password;
}

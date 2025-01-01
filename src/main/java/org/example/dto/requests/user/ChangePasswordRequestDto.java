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
public class ChangePasswordRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Old password is required")
    private String oldPassword;

    @NotNull(message = "New Password is required")
    private String password;

}

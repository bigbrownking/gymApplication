package org.example.dto.requests.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangePasswordRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Old password is required")
    private String oldPassword;

    @NotNull(message = "New Password is required")
    private String password;

}

package org.example.dto.requests.trainee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTraineeRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Firstname is required")
    private String firstName;

    @NotNull(message = "Lastname is required")
    private String lastName;

    @Nullable
    private LocalDateTime dateOfBirth;

    @Nullable
    private String address;

    @NotNull(message = "IsActive is required")
    private boolean isActive;
}

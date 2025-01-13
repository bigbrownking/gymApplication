package org.example.dto.requests.trainee;

import lombok.*;
import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateTraineeRequestDto {
    @NotNull(message = "First name is required")
    private String firstName;

    @NotNull(message = "Last name is required")
    private String lastName;

    @Nullable
    private LocalDateTime dateOfBirth;

    @Nullable
    private String address;
}

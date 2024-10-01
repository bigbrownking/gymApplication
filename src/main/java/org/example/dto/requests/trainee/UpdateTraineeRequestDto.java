package org.example.dto.requests.trainee;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class UpdateTraineeRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @NotNull(message = "Firstname is required")
    private String firstName;

    @NotNull(message = "Lastname is required")
    private String lastName;

    @Nullable
    private Date dateOfBirth;

    @Nullable
    private String address;

    @NotNull(message = "IsActive is required")
    private boolean isActive;
}

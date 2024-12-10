package org.example.dto.requests.trainer;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class GetTrainerTrainingListRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @Nullable
    private LocalDateTime fromDate;

    @Nullable
    private LocalDateTime toDate;

    @Nullable
    private String traineeName;
}

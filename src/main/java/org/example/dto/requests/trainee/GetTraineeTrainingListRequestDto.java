package org.example.dto.requests.trainee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.TrainingTypeEntity;
import org.springframework.lang.Nullable;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetTraineeTrainingListRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @Nullable
    private LocalDateTime fromDate;

    @Nullable
    private LocalDateTime toDate;

    @Nullable
    private String trainerName;

    @Nullable
    private TrainingTypeEntity trainingType;
}

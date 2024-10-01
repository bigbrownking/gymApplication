package org.example.dto.requests.trainee;

import lombok.Getter;
import lombok.Setter;
import org.example.models.TrainingTypeEntity;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class GetTraineeTrainingListRequestDto {

    @NotNull(message = "Username is required")
    private String username;

    @Nullable
    private Date fromDate;

    @Nullable
    private Date toDate;

    @Nullable
    private String trainerName;

    @Nullable
    private TrainingTypeEntity trainingType;
}

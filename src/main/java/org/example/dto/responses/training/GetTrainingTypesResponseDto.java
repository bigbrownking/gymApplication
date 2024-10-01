package org.example.dto.responses.training;

import lombok.Getter;
import lombok.Setter;
import org.example.models.TrainingTypeEntity;

import java.util.List;

@Getter
@Setter
public class GetTrainingTypesResponseDto {

    List<TrainingTypeEntity> trainingTypeEntities;
}

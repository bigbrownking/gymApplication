package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.models.TrainingTypeEntity;

import java.util.Date;

@Getter
@Setter
public class TrainingDto {

    private String trainingName;
    private Date trainingDate;
    private TrainingTypeEntity trainingType;
    private Integer duration;
    private String traineeName;
}

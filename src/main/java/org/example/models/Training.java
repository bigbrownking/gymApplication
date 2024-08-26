package org.example.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Training {
    private Long trainingId;
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private TrainingType trainingType;
    private String trainingDate;
    private String trainingDuration;


    @Override
    public String toString() {
        return "Training{" +
                "trainingId=" + trainingId +
                ", traineeId=" + traineeId +
                ", trainerId=" + trainerId +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate='" + trainingDate + '\'' +
                ", trainingDuration='" + trainingDuration + '\'' +
                '}';
    }
}

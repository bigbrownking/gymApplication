package org.example.models;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long trainingId;

    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "training_type_id")
    private TrainingTypeEntity trainingType;

    @Column(name = "training_date", nullable = false)
    private Date trainingDate;

    @Column(name = "training_duration", nullable = false)
    private Integer trainingDuration;


    @Override
    public String toString() {
        return "Training{" +
                "trainingId=" + trainingId +
                ", traineeId=" + trainee.getTraineeId() +
                ", trainerId=" + trainer.getTrainerId() +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDate='" + trainingDate + '\'' +
                ", trainingDuration='" + trainingDuration + '\'' +
                '}';
    }
}

package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.models.TrainingTypeEntity;

@Getter
@Setter
public class TrainerDto {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeEntity specialization;
}

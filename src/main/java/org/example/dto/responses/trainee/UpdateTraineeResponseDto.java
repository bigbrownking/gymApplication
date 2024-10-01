package org.example.dto.responses.trainee;

import lombok.Getter;
import lombok.Setter;
import org.example.dto.TrainerDto;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UpdateTraineeResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String address;
    private boolean isActive;

    private List<TrainerDto> trainerDtoList;
}

package org.example.dto.responses.trainee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.dto.TrainerDto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTraineeResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime dateOfBirth;
    private String address;
    private boolean isActive;

    private List<TrainerDto> trainerDtoList;
}

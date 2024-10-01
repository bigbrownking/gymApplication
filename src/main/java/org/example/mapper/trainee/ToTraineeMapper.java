package org.example.mapper.trainee;

import org.example.dto.requests.trainee.*;
import org.example.models.Trainee;

public interface ToTraineeMapper {
    Trainee toTrainee(CreateTraineeRequestDto createTraineeRequestDto);
    String toDeleteByUsernameTrainee(DeleteTraineeRequestDto deleteTraineeRequestDto);
    String toGetNotAssignedTrainersByUsernameTrainee(GetNotAssignedTrainersRequestDto getNotAssignedTrainersRequestDto);
    String toGetUsernameTrainee(GetTraineeByUsernameRequestDto getTraineeByUsernameRequestDto);
    Trainee toTrainee(UpdateTraineeRequestDto updateTraineeRequestDto);

}

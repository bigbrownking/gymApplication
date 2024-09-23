package org.example.service;

import org.example.dto.requests.trainee.*;
import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.dto.responses.trainee.*;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.Date;
import java.util.List;

public interface TraineeService {
    CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto createTraineeRequestDto);

    UpdateTraineeResponseDto updateTrainee(UpdateTraineeRequestDto updateTraineeRequestDto);

    void deleteTrainee(DeleteTraineeRequestDto deleteTraineeRequestDto);

    GetTraineeByUsernameResponseDto getTraineeByUsername(GetTraineeByUsernameRequestDto getTraineeByUsernameRequestDto);
    void getTraineeByUsernameAndPassword(LoginRequestDto loginRequestDto);
    List<Trainee> getAllTrainee();
    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);

    void activateTrainee(ActivateUserRequestDto activateUserRequestDto);

    void deactivateTrainee(DeactivateUserRequestDto deactivateUserRequestDto);
    GetTraineeTrainingListResponseDto getTrainingByCriteria(GetTraineeTrainingListRequestDto getTraineeTrainingListRequestDto);

    GetNotAssignedTrainersResponseDto getTrainersNotAssignedToTrainee(GetNotAssignedTrainersRequestDto getNotAssignedTrainersRequestDto);
}

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
    CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto createTraineeRequestDto) throws Exception;

    UpdateTraineeResponseDto updateTrainee(UpdateTraineeRequestDto updateTraineeRequestDto) throws Exception;

    void deleteTrainee(DeleteTraineeRequestDto deleteTraineeRequestDto) throws Exception;

    GetTraineeByUsernameResponseDto getTraineeByUsername(GetTraineeByUsernameRequestDto getTraineeByUsernameRequestDto) throws Exception;
    void getTraineeByUsernameAndPassword(LoginRequestDto loginRequestDto) throws Exception;
    void changePassword(ChangePasswordRequestDto changePasswordRequestDto) throws Exception;

    void activateTrainee(ActivateUserRequestDto activateUserRequestDto) throws Exception;

    void deactivateTrainee(DeactivateUserRequestDto deactivateUserRequestDto) throws Exception;
    GetTraineeTrainingListResponseDto getTrainingByCriteria(GetTraineeTrainingListRequestDto getTraineeTrainingListRequestDto) throws Exception;

    GetNotAssignedTrainersResponseDto getTrainersNotAssignedToTrainee(GetNotAssignedTrainersRequestDto getNotAssignedTrainersRequestDto) throws Exception;
}

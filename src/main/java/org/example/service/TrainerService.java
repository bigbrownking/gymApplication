package org.example.service;

import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerByUsernameRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.dto.responses.trainer.UpdateTrainerResponseDto;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;

import java.util.Date;
import java.util.List;

public interface TrainerService {
    CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto createTrainerRequestDto);
    UpdateTrainerResponseDto updateTrainer(UpdateTrainerRequestDto updateTrainerRequestDto);
    GetTrainerByUsernameResponseDto getTrainerByUsername(GetTrainerByUsernameRequestDto getTrainerByUsernameRequestDto);
    void getTrainerByUsernameAndPassword(LoginRequestDto loginRequestDto);
    List<Trainer> getAllTrainers();
    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);
    void activateTrainer(ActivateUserRequestDto activateUserRequestDto);
    void deactivateTrainer(DeactivateUserRequestDto deactivateUserRequestDto);
    GetTrainerTrainingListResponseDto getTrainingByCriteria(GetTrainerTrainingListRequestDto getTrainerTrainingListRequestDto);
}

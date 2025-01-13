package org.example.service;

import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.requests.user.GetProfileRequest;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.dto.responses.trainer.UpdateTrainerResponseDto;

public interface TrainerService {
    CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto createTrainerRequestDto);
    UpdateTrainerResponseDto updateTrainer(UpdateTrainerRequestDto updateTrainerRequestDto);
    GetTrainerByUsernameResponseDto getTrainerByUsername(GetProfileRequest getTrainerByUsernameRequestDto);
    void changePassword(ChangePasswordRequestDto changePasswordRequestDto);
    void activateTrainer(ActivateUserRequestDto activateUserRequestDto);
    void deactivateTrainer(DeactivateUserRequestDto deactivateUserRequestDto);
    GetTrainerTrainingListResponseDto getTrainingByCriteria(GetTrainerTrainingListRequestDto getTrainerTrainingListRequestDto);
    String getPasswordFromTrainer(String username);
}

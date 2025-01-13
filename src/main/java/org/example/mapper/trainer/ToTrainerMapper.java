package org.example.mapper.trainer;

import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.requests.user.GetProfileRequest;
import org.example.models.Trainer;

public interface ToTrainerMapper{
    Trainer toTrainer(CreateTrainerRequestDto createTrainerRequestDto);
    String toTrainer(GetProfileRequest getTrainerByUsernameRequestDto);
    Trainer toTrainer(UpdateTrainerRequestDto updateTrainerRequestDto);
}

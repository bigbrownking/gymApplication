package org.example.mapper.trainer;

import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerByUsernameRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.models.Trainer;

public interface ToTrainerMapper{
    Trainer toTrainer(CreateTrainerRequestDto createTrainerRequestDto);
    String toTrainer(GetTrainerByUsernameRequestDto getTrainerByUsernameRequestDto);
    Trainer toTrainer(UpdateTrainerRequestDto updateTrainerRequestDto);
}

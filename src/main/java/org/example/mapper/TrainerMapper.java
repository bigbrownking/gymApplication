package org.example.mapper;

import org.example.dto.TrainerDto;
import org.example.mapper.trainer.ToTrainerDtoMapper;
import org.example.mapper.trainer.ToTrainerMapper;
import org.example.models.Trainer;

import java.util.List;

public interface TrainerMapper extends ToTrainerMapper, ToTrainerDtoMapper {
    List<TrainerDto> convertTrainersToDto(List<Trainer> trainers);
}

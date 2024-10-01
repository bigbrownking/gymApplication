package org.example.mapper.impl;

import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerByUsernameRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.dto.responses.trainer.UpdateTrainerResponseDto;
import org.example.mapper.TrainerMapper;
import org.example.models.Trainer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainerMapperImpl implements TrainerMapper {
    @Override
    public CreateTrainerResponseDto toCreateTrainerDto(Trainer trainer) {
        if(trainer == null){
            return null;
        }

        CreateTrainerResponseDto dto = new CreateTrainerResponseDto();
        dto.setUsername(trainer.getUsername());
        dto.setPassword(trainer.getPassword());

        return dto;
    }

    @Override
    public GetTrainerByUsernameResponseDto toGetTrainerByUsernameDto(Trainer trainer, List<TraineeDto> traineeDtos) {
        if(trainer == null){
            return null;
        }

        GetTrainerByUsernameResponseDto dto = new GetTrainerByUsernameResponseDto();
        dto.setFirstname(trainer.getFirstName());
        dto.setLastname(trainer.getLastName());
        dto.setSpecialization(trainer.getSpecialization());
        dto.setActive(trainer.isActive());
        dto.setTraineeDtos(traineeDtos);

        return dto;

    }

    @Override
    public GetTrainerTrainingListResponseDto toGetTrainingListDto(List<TrainingDto> trainingDtos) {
       if(trainingDtos == null){
           return null;
       }

       GetTrainerTrainingListResponseDto dto = new GetTrainerTrainingListResponseDto();
       dto.setTrainingDtos(trainingDtos);

       return dto;

    }

    @Override
    public UpdateTrainerResponseDto toUpdateTrainerDto(Trainer trainer, List<TraineeDto> traineeDtos) {
        if(trainer == null){
            return null;
        }

        UpdateTrainerResponseDto dto = new UpdateTrainerResponseDto();
        dto.setUsername(trainer.getUsername());
        dto.setFirstName(trainer.getFirstName());
        dto.setLastName(trainer.getLastName());
        dto.setSpecialization(trainer.getSpecialization());
        dto.setActive(trainer.isActive());
        dto.setTraineeDtos(traineeDtos);

        return dto;
    }

    @Override
    public Trainer toTrainer(CreateTrainerRequestDto createTrainerRequestDto) {
        if(createTrainerRequestDto == null){
            return null;
        }

        Trainer trainer = new Trainer();
        trainer.setFirstName(createTrainerRequestDto.getFirstName());
        trainer.setLastName(createTrainerRequestDto.getLastName());
        trainer.setSpecialization(createTrainerRequestDto.getSpecialization());

        return trainer;
    }

    @Override
    public String toTrainer(GetTrainerByUsernameRequestDto getTrainerByUsernameRequestDto) {
        if(getTrainerByUsernameRequestDto == null){
            return null;
        }

        return getTrainerByUsernameRequestDto.getUsername();
    }

    @Override
    public Trainer toTrainer(UpdateTrainerRequestDto updateTrainerRequestDto) {
        if(updateTrainerRequestDto == null) {
            return null;
        }

        Trainer trainer = new Trainer();
        trainer.setUsername(updateTrainerRequestDto.getUsername());
        trainer.setFirstName(updateTrainerRequestDto.getFirstName());
        trainer.setLastName(updateTrainerRequestDto.getLastName());
        trainer.setSpecialization(updateTrainerRequestDto.getSpecialization());
        trainer.setActive(updateTrainerRequestDto.isActive());

        return trainer;
    }

    @Override
    public List<TrainerDto> convertTrainersToDto(List<Trainer> trainers) {
        if(trainers == null){
            return null;
        }

        List<TrainerDto> dtos = new ArrayList<>();
        for(Trainer trainer : trainers){
            TrainerDto dto = new TrainerDto();
            dto.setFirstName(trainer.getFirstName());
            dto.setLastName(trainer.getLastName());
            dto.setSpecialization(trainer.getSpecialization());
            dto.setUsername(trainer.getUsername());

            dtos.add(dto);
        }
        return dtos;
    }
}

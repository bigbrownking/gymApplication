package org.example.mapper.impl;

import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.requests.trainee.*;
import org.example.dto.responses.trainee.*;
import org.example.mapper.TraineeMapper;
import org.example.models.Trainee;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class TraineeMapperImpl implements TraineeMapper {

    @Override
    public Trainee toTrainee(CreateTraineeRequestDto createTraineeRequestDto) {
        if(createTraineeRequestDto == null){
            return null;
        }

        Trainee trainee = new Trainee();
        trainee.setFirstName(createTraineeRequestDto.getFirstName());
        trainee.setLastName(createTraineeRequestDto.getLastName());
        trainee.setDateOfBirth(createTraineeRequestDto.getDateOfBirth());
        trainee.setAddress(createTraineeRequestDto.getAddress());

        return trainee;
    }

    @Override
    public CreateTraineeResponseDto toCreateTraineeDto(Trainee trainee) {
        if(trainee == null){
            return null;
        }

        CreateTraineeResponseDto dto = new CreateTraineeResponseDto();
        dto.setUsername(trainee.getUsername());
        dto.setPassword(trainee.getPassword());
        return dto;
    }

    @Override
    public GetNotAssignedTrainersResponseDto toGetNotAssignedTrainersDto(List<TrainerDto> trainerDtos) {
        if(trainerDtos == null){
            return null;
        }

        GetNotAssignedTrainersResponseDto dto =  new GetNotAssignedTrainersResponseDto();
        dto.setTrainerDtos(trainerDtos);

        return dto;
    }

    @Override
    public GetTraineeByUsernameResponseDto toGetTraineeByUsernameDto(Trainee trainee, List<TrainerDto> trainerDtos) {
        if(trainee == null){
            return null;
        }

        GetTraineeByUsernameResponseDto dto = new GetTraineeByUsernameResponseDto();
        dto.setFirstName(trainee.getFirstName());
        dto.setLastName(trainee.getLastName());
        dto.setDateOfBirth(trainee.getDateOfBirth());
        dto.setAddress(trainee.getAddress());
        dto.setActive(trainee.isActive());
        dto.setTrainerDtos(trainerDtos);
        return dto;
    }

    @Override
    public GetTraineeTrainingListResponseDto toGetTraineeTrainingListDto(List<TrainingDto> trainingDtos) {
        if(trainingDtos == null){
            return null;
        }

        GetTraineeTrainingListResponseDto dto = new GetTraineeTrainingListResponseDto();
        dto.setTrainingDtos(trainingDtos);

        return dto;
    }

    @Override
    public UpdateTraineeResponseDto toUpdateTraineeDto(Trainee trainee, List<TrainerDto> trainerDtos) {
        if(trainee == null){
            return null;
        }

        UpdateTraineeResponseDto dto = new UpdateTraineeResponseDto();
        dto.setUsername(trainee.getUsername());
        dto.setFirstName(trainee.getFirstName());
        dto.setLastName(trainee.getLastName());
        dto.setDateOfBirth(trainee.getDateOfBirth());
        dto.setAddress(trainee.getAddress());
        dto.setActive(trainee.isActive());
        dto.setTrainerDtoList(trainerDtos);
        return dto;
    }

    @Override
    public String toDeleteByUsernameTrainee(DeleteTraineeRequestDto deleteTraineeRequestDto) {
        if(deleteTraineeRequestDto == null){
            return null;
        }

        return deleteTraineeRequestDto.getUsername();
    }

    @Override
    public String toGetNotAssignedTrainersByUsernameTrainee(GetNotAssignedTrainersRequestDto getNotAssignedTrainersRequestDto) {
        if(getNotAssignedTrainersRequestDto == null){
            return null;
        }

        return getNotAssignedTrainersRequestDto.getUsername();
    }

    @Override
    public String toGetUsernameTrainee(GetTraineeByUsernameRequestDto getTraineeByUsernameRequestDto) {
        if(getTraineeByUsernameRequestDto == null){
            return null;
        }

        return getTraineeByUsernameRequestDto.getUsername();
    }

    @Override
    public Trainee toTrainee(UpdateTraineeRequestDto updateTraineeRequestDto) {
        if(updateTraineeRequestDto == null){
            return null;
        }

        Trainee trainee = new Trainee();
        trainee.setUsername(updateTraineeRequestDto.getUsername());
        trainee.setFirstName(updateTraineeRequestDto.getFirstName());
        trainee.setLastName(updateTraineeRequestDto.getLastName());
        trainee.setDateOfBirth(updateTraineeRequestDto.getDateOfBirth());
        trainee.setAddress(updateTraineeRequestDto.getAddress());
        trainee.setActive(updateTraineeRequestDto.isActive());

        return trainee;
    }

    @Override
    public List<TraineeDto> convertTraineesToDto(List<Trainee> trainees) {
        if(trainees == null){
            return null;
        }

        List<TraineeDto> dtos = new ArrayList<>();
        for(Trainee trainee : trainees){
            TraineeDto dto = new TraineeDto();
            dto.setFirstName(trainee.getFirstName());
            dto.setLastName(trainee.getLastName());
            dto.setUsername(trainee.getUsername());

            dtos.add(dto);
        }

        return dtos;
    }

}

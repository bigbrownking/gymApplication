package org.example.facade;


import org.example.dto.requests.trainee.*;
import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerByUsernameRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.dto.responses.trainee.*;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class TrainingFacade {
    private final TrainerService trainerService;
    private final TraineeService traineeService;
    private final TrainingService trainingService;

    @Autowired
    public TrainingFacade(TrainerService trainerService, TraineeService traineeService, TrainingService trainingService) {
        this.trainerService = trainerService;
        this.traineeService = traineeService;
        this.trainingService = trainingService;
    }

    public List<Trainer> getAllTrainers(){
        return trainerService.getAllTrainers();
    }
    public CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto createTrainerRequestDto){
        return trainerService.createTrainer(createTrainerRequestDto);
    }
    public void deleteTrainee(DeleteTraineeRequestDto deleteTraineeRequestDto){
         traineeService.deleteTrainee(deleteTraineeRequestDto);
     }
    public CreateTraineeResponseDto addTrainee(CreateTraineeRequestDto createTraineeRequestDto){
        return traineeService.createTrainee(createTraineeRequestDto);
    }
    public GetTraineeByUsernameResponseDto getByUsernameTrainee(GetTraineeByUsernameRequestDto getTraineeByUsernameRequestDto){
        return traineeService.getTraineeByUsername(getTraineeByUsernameRequestDto);
    }
    public void getByUsernameAndPassword(LoginRequestDto loginRequestDto){
        traineeService.getTraineeByUsernameAndPassword(loginRequestDto);
    }
    public UpdateTraineeResponseDto updateTrainee(UpdateTraineeRequestDto updateTraineeRequestDto){
        return traineeService.updateTrainee(updateTraineeRequestDto);
    }
    public void updatePasswordForTrainee(ChangePasswordRequestDto changePasswordRequestDto){
        traineeService.changePassword(changePasswordRequestDto);
    }
    public void activateTrainee(ActivateUserRequestDto activateUserRequestDto){
        traineeService.activateTrainee(activateUserRequestDto);
    }
    public void deactivateTrainee(DeactivateUserRequestDto deactivateUserRequestDto){
        traineeService.deactivateTrainee(deactivateUserRequestDto);
    }

    public void createTraining(CreateTrainingRequestDto createTrainingRequestDto){
        trainingService.createTraining(createTrainingRequestDto);
    }
    public GetTrainerByUsernameResponseDto getByUsernameTrainer(GetTrainerByUsernameRequestDto getTrainerByUsernameRequestDto){
        return trainerService.getTrainerByUsername(getTrainerByUsernameRequestDto);
    }
    public GetTraineeTrainingListResponseDto getTrainingTraineeListByCriteria(GetTraineeTrainingListRequestDto getTraineeTrainingListRequestDto){
        return traineeService.getTrainingByCriteria(getTraineeTrainingListRequestDto);
    }
    public GetTrainerTrainingListResponseDto getTrainingTrainerListByCriteria(GetTrainerTrainingListRequestDto getTrainerTrainingListRequestDto){
        return trainerService.getTrainingByCriteria(getTrainerTrainingListRequestDto);
    }
    public GetNotAssignedTrainersResponseDto getTrainersNotAssignedTrainee(GetNotAssignedTrainersRequestDto getNotAssignedTrainersRequestDto){
        return traineeService.getTrainersNotAssignedToTrainee(getNotAssignedTrainersRequestDto);
    }
}
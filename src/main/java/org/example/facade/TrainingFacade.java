package org.example.facade;


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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public void createTrainer(Trainer trainer){
        trainerService.createTrainer(trainer);
    }

    public List<Training> getAllTrainings(){
        return trainingService.getAllTrainings();
    }
   /* public void deleteTrainee(Long id){
        traineeService.deleteTrainee(id);
    }*/
    public List<Trainee> getAllTrainees(){
        return traineeService.getAllTrainee();
    }
    public void addTrainee(Trainee trainee){
        traineeService.createTrainee(trainee);
    }
    public Trainee getByUsernameTrainee(String username){
        return traineeService.getTraineeByUsername(username);
    }
    public Trainee getByUsernameAndPassword(String username, String password){
        return traineeService.getTraineeByUsernameAndPassword(username, password);
    }
    public void updateTrainee(Trainee trainee){
        traineeService.updateTrainee(trainee);
    }
    public void updatePasswordForTrainee(Trainee trainee, String password){
        traineeService.changePassword(trainee, password);
    }
    public void activateTrainee(String username){
        traineeService.activateTrainee(username);
    }
    public void deactivateTrainee(String username){
        traineeService.deactivateTrainee(username);
    }

    public void createTraining(Training training){
        trainingService.createTraining(training);
    }
    public Trainer getByUsernameTrainer(String username){
        return trainerService.getTrainerByUsername(username);
    }
    public List<Training> getTrainingTraineeListByCriteria(String username, Date startDate, Date endDate, String trainerName, TrainingTypeEntity trainingType){
        return traineeService.getTrainingByCriteria(username, startDate, endDate, trainerName, trainingType);
    }
    public TrainingTypeEntity getTrainingType(String trainingType){
        return trainingService.getTrainingType(trainingType);
    }
    public List<Training> getTrainingTrainerListByCriteria(String username, Date startDate, Date endDate, String traineeName){
        return trainerService.getTrainingByCriteria(username, startDate, endDate, traineeName);
    }
    public List<Trainer> getTrainerNotAssignedTrainee(String username){
        return traineeService.getTrainersNotAssignedToTrainee(username);
    }
    public void deleteTrainee(String username){
        traineeService.deleteTrainee(username);
    }
}

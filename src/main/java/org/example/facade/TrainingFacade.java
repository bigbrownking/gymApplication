package org.example.facade;


import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public void deleteTrainee(Long id){
        traineeService.deleteTrainee(id);
    }
    public List<Trainee> getAllTrainees(){
        return traineeService.getAllTrainee();
    }
}

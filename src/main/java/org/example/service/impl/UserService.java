package org.example.service.impl;

import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    private TraineeService traineeService;

    private TrainerService trainerService;
    private TrainingService trainingService;

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setTrainingService(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    private List<String> trainerUsernames(){
        return trainerService.getAllTrainers().stream()
                .map(Trainer::getUsername)
                .toList();
    }
    private List<String> traineeUsernames(){
        return traineeService.getAllTrainee().stream()
                .map(Trainee::getUsername)
                .toList();
    }
    public List<String> getAllExistingUsernames() {
        return Stream.concat(trainerUsernames().stream(), traineeUsernames().stream())
                .collect(Collectors.toList());
    }

    public void removeTraineeFromTraining(Long traineeId){
        List<Training> trainingsWithTrainee = trainingService.getAllTrainings().stream()
                .filter(training -> training.getTraineeId().equals(traineeId))
                .toList();

        for (Training training : trainingsWithTrainee) {
            training.setTraineeId(null);
            trainingService.updateTraining(training);
        }
    }

}

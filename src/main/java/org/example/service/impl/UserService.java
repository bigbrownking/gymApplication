package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.models.*;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private TraineeDao traineeDao;
    private TrainerDao trainerDao;

    @Autowired
    public void setTraineeService(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setTrainerService(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    private List<String> trainerUsernames() throws Exception {
        return trainerDao.listAll().stream()
                .map(Trainer::getUsername)
                .toList();
    }

    private List<String> traineeUsernames() throws Exception {
        return traineeDao.listAll().stream()
                .map(Trainee::getUsername)
                .toList();
    }

    public List<String> getAllExistingUsernames() throws Exception {
        return Stream.concat(trainerUsernames().stream(), traineeUsernames().stream())
                .collect(Collectors.toList());
    }

    public boolean isAuthenticated(String username, String password) throws Exception {
        return traineeDao.findByUsernameAndPassword(username, password).isPresent()
                || trainerDao.findByUsernameAndPassword(username, password).isPresent();
    }

    public boolean isValid(User user) {
        if (user instanceof Trainee) {
            return user.getFirstName() != null &&
                    user.getLastName() != null;
        } else if (user instanceof Trainer) {
            return user.getFirstName() != null &&
                    user.getLastName() != null &&
                    trainerDao.isSpecializationValid(((Trainer) user).getSpecialization());
        }
        return false;
    }
    public void activate(User user) {
        if (user != null && !user.isActive() && user instanceof Trainee){
            user.setActive(true);
            LOGGER.debug("Activating trainee...");
            traineeDao.update((Trainee) user);
        }else if(user != null && !user.isActive() && user instanceof Trainer){
            user.setActive(true);
            LOGGER.debug("Activating trainer...");
            trainerDao.update((Trainer) user);
        }
        else{
            LOGGER.warn("User is already active...");
        }
    }

    public void deactivate(User user) {
        if (user != null && user.isActive() && user instanceof Trainee){
            user.setActive(false);
            LOGGER.debug("Deactivating trainee...");
            traineeDao.update((Trainee) user);
        }else if(user != null && user.isActive() && user instanceof Trainer){
            user.setActive(false);
            LOGGER.debug("Deactivating trainer...");
            trainerDao.update((Trainer) user);
        }
        else{
            LOGGER.warn("User is already not active...");
        }
    }


}

package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.InvalidDataException;
import org.example.models.*;
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

    private List<String> trainerUsernames(){
        return trainerDao.listAll().stream()
                .map(Trainer::getUsername)
                .toList();
    }

    private List<String> traineeUsernames() {
        return traineeDao.listAll().stream()
                .map(Trainee::getUsername)
                .toList();
    }

    public List<String> getAllExistingUsernames() {
        return Stream.concat(trainerUsernames().stream(), traineeUsernames().stream())
                .collect(Collectors.toList());
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
        try {
            if (user != null && !user.isActive() && user instanceof Trainee) {
                user.setActive(true);
                LOGGER.debug("Activating trainee...");
                traineeDao.update((Trainee) user);
            } else if (user != null && !user.isActive() && user instanceof Trainer) {
                user.setActive(true);
                LOGGER.debug("Activating trainer...");
                trainerDao.update((Trainer) user);
            } else {
                LOGGER.warn("User is already active...");
            }
        } catch (EntityNotFoundException | InvalidDataException e) {
            LOGGER.warn("Entity not found or data is invalid...");
        }
    }

    public void deactivate(User user) {
        try {
            if (user != null && user.isActive() && user instanceof Trainee) {
                user.setActive(false);
                LOGGER.debug("Deactivating trainee...");
                traineeDao.update((Trainee) user);
            } else if (user != null && user.isActive() && user instanceof Trainer) {
                user.setActive(false);
                LOGGER.debug("Deactivating trainer...");
                trainerDao.update((Trainer) user);
            } else {
                LOGGER.warn("User is already not active...");
            }
        } catch (EntityNotFoundException | InvalidDataException e) {
            LOGGER.warn("Entity not found or data is invalid...");
        }
    }


}

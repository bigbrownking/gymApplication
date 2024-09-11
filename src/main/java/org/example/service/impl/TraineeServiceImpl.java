package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.example.service.TraineeService;
import org.example.util.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeDao traineeDao;
    private UserService userService;

    @Autowired
    public TraineeServiceImpl(TraineeDao traineeDao, UserService userService) {
        this.traineeDao = traineeDao;
        this.userService = userService;
    }

    @Override
    public void createTrainee(Trainee trainee) {
        LOGGER.debug("Creating new trainee...");
        if (!userService.isValid(trainee)) {
            LOGGER.warn("Invalid trainer...");
            return;
        }
        List<String> existingUsernames = userService.getAllExistingUsernames();

        LOGGER.debug("Generating new username and password...");
        String username = Generator.generateUsername(trainee.getFirstName(), trainee.getLastName(), existingUsernames);
        String password = Generator.generatePassword();

        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);

        traineeDao.create(trainee);
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        if (!userService.isAuthenticated(trainee.getUsername(), trainee.getPassword())) {
            LOGGER.warn("Authentication failed for trainee: " + trainee.getUsername());
            return;
        }
        if (!userService.isValid(trainee)) {
            LOGGER.warn("Invalid trainee...");
            return;
        }
        LOGGER.debug("Updating current trainee...");
        traineeDao.update(trainee);
    }

    @Override
    public void deleteTrainee(String username) {
        LOGGER.debug("Deleting trainee...");
        boolean isDeleted = traineeDao.delete(username);
        if (!isDeleted) {
            LOGGER.warn("Trainee wasn't found...");
        }
        LOGGER.debug("Trainee was deleted...");
    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        LOGGER.debug("Retrieving a trainee by username...");
        return traineeDao.findByUsername(username).orElse(null);
    }

    @Override
    public List<Trainee> getAllTrainee() {
        LOGGER.debug("Retrieving all trainees...");
        return traineeDao.listAll();
    }

    @Override
    public Trainee getTraineeByUsernameAndPassword(String username, String password) {
        if (!userService.isAuthenticated(username, password)) {
            LOGGER.warn("Authentication failed for trainee: " + username);
            return null;
        }
        LOGGER.debug("Retrieving a trainee by username and password...");
        return traineeDao.findByUsernameAndPassword(username, password).orElse(null);
    }

    @Override
    public void changePassword(Trainee trainee, String password) {
        if (!userService.isAuthenticated(trainee.getUsername(), trainee.getPassword())) {
            LOGGER.warn("Authentication failed for trainee: " + trainee.getUsername());
            return;
        }
        LOGGER.debug("Changing password for trainee...");
        trainee.setPassword(password);
        traineeDao.update(trainee);
    }

    @Override
    public void activateTrainee(String username) {
        Trainee trainee = traineeDao.findByUsername(username).orElse(null);
        if (trainee == null) {
            LOGGER.warn("There is no such trainee found...");
            return;
        }
        userService.activate(trainee);
    }

    @Override
    public void deactivateTrainee(String username) {
        Trainee trainee = traineeDao.findByUsername(username).orElse(null);
        if (trainee == null) {
            LOGGER.warn("There is no such trainee found...");
            return;
        }
        userService.deactivate(trainee);
    }

    @Override
    public List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String trainerName, TrainingTypeEntity trainingType) {
        LOGGER.debug("Getting training list by criteria...");
        return traineeDao.getTrainingByCriteria(username, startDate, endDate, trainerName, trainingType);
    }

    @Override
    public List<Trainer> getTrainersNotAssignedToTrainee(String username) {
        LOGGER.debug("Getting trainers that are not assigned to trainee...");
        return traineeDao.getTrainersNotAssignedToTrainee(username);
    }

}

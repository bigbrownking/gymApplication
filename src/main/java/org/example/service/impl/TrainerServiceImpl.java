package org.example.service.impl;

import org.example.dao.TrainerDao;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.util.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private final TrainerDao trainerDao;
    private UserService userService;

    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao, UserService userService) {
        this.trainerDao = trainerDao;
        this.userService = userService;
    }

    @Override
    public void createTrainer(Trainer trainer) {
        LOGGER.debug("Creating new trainer...");
        if (!userService.isValid(trainer)) {
            LOGGER.warn("Invalid trainer...");
            return;
        }
        List<String> existingUsernames = userService.getAllExistingUsernames();

        LOGGER.debug("Generating new username and password...");
        String username = Generator.generateUsername(trainer.getFirstName(), trainer.getLastName(), existingUsernames);
        String password = Generator.generatePassword();

        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);

        trainerDao.create(trainer);
    }

    @Override
    public void updateTrainer(Trainer trainer) {
        if (!userService.isAuthenticated(trainer.getUsername(), trainer.getPassword())) {
            LOGGER.warn("Authentication failed for trainer: " + trainer.getUsername());
            return;
        }
        if (!userService.isValid(trainer)) {
            LOGGER.warn("Invalid trainer...");
            return;
        }
        LOGGER.debug("Updating current trainer...");
        trainerDao.update(trainer);
    }

    @Override
    public Trainer getTrainerByUsername(String username) {
        LOGGER.debug("Retrieving a trainer by username...");
        return trainerDao.findByUsername(username).orElse(null);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        LOGGER.debug("Retrieving all trainers...");
        return trainerDao.listAll();
    }

    @Override
    public Trainer getTrainerByUsernameAndPassword(String username, String password) {
        if (!userService.isAuthenticated(username, password)) {
            LOGGER.warn("Authentication failed for trainer: " + username);
            return null;
        }
        LOGGER.debug("Retrieving a trainer by username and password...");
        return trainerDao.findByUsernameAndPassword(username, password).orElse(null);
    }


    @Override
    public void changePassword(Trainer trainer, String password) {
        if (!userService.isAuthenticated(trainer.getUsername(), trainer.getPassword())) {
            LOGGER.warn("Authentication failed for trainer: " + trainer.getUsername());
            return;
        }
        LOGGER.debug("Changing password for trainer...");
        trainer.setPassword(password);
        trainerDao.update(trainer);
    }

    @Override
    public void activateTrainer(String username) {
        Trainer trainer = trainerDao.findByUsername(username).orElse(null);
        if (trainer == null) {
            LOGGER.warn("There is no such trainer found...");
            return;
        }
        userService.activate(trainer);
    }

    @Override
    public void deactivateTrainer(String username) {
        Trainer trainer = trainerDao.findByUsername(username).orElse(null);
        if (trainer == null) {
            LOGGER.warn("There is no such trainer found...");
            return;
        }
        userService.deactivate(trainer);
    }

    @Override
    public List<Training> getTrainingByCriteria(String username, Date startDate, Date endDate, String traineeName) {
        LOGGER.debug("Getting training by criteria...");
        return trainerDao.getTrainingByCriteria(username, startDate, endDate, traineeName);
    }
}

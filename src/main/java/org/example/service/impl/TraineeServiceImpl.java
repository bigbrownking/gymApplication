package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.models.Trainee;
import org.example.service.TraineeService;
import org.example.util.LogUtil;
import org.example.util.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeDao traineeDao;
    private UserService userService;

    @Autowired
    public TraineeServiceImpl(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    @Autowired
    public void setUserService(UserService userService) {
         this.userService = userService;
    }

    @Override
    public void createTrainee(Trainee trainee) {
        LOGGER.info("Creating new trainee...");

        List<String> existingUsernames = userService.getAllExistingUsernames();

        LogUtil.info(TraineeService.class, "Generating new username and password...");
        String username = Generator.generateUsername(trainee.getFirstName(), trainee.getLastName(), existingUsernames);
        String password = Generator.generatePassword();
        Long userId = Generator.generateId(traineeDao.listAll());

        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setUserId(userId);

        traineeDao.create(trainee);
    }

    @Override
    public void updateTrainee(Trainee trainee) {
        LOGGER.info("Updating current trainee...");
        traineeDao.update(trainee);
    }

    @Override
    public void deleteTrainee(Long userId) {
        LOGGER.info("Deleting trainee...");
        userService.removeTraineeFromTraining(userId);
        traineeDao.delete(userId);
    }

    @Override
    public Trainee getTraineeByUsername(String username) {
        LOGGER.info("Retrieving a trainee...");
        return traineeDao.findByUsername(username).orElse(null);
    }

    @Override
    public List<Trainee> getAllTrainee() {
        LOGGER.info("Retrieving all trainees...");
        return traineeDao.listAll();
    }
}

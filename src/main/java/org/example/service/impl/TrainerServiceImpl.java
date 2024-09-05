package org.example.service.impl;

import org.example.dao.TrainerDao;
import org.example.models.Trainer;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.util.LogUtil;
import org.example.util.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private final TrainerDao trainerDao;
    private UserService userService;

    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;

    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }



    @Override
    public void createTrainer(Trainer trainer) {
        LOGGER.info("Creating new trainer...");

        List<String> existingUsernames = userService.getAllExistingUsernames();

        LogUtil.info(TraineeService.class, "Generating new username and password...");
        String username = Generator.generateUsername(trainer.getFirstName(), trainer.getLastName(), existingUsernames);
        String password = Generator.generatePassword();
        Long userId = Generator.generateId(trainerDao.listAll());

        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setUserId(userId);

        trainerDao.create(trainer);
    }
    @Override
    public void updateTrainer(Trainer trainer) {
        LOGGER.info("Updating current trainer...");
        trainerDao.update(trainer);
    }
    @Override
    public Trainer getTrainer(String username) {
        LOGGER.info("Retrieving a trainer...");
        return trainerDao.findByUsername(username).orElse(null);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        LOGGER.info("Retrieving all trainers...");
        return trainerDao.listAll();
    }
}

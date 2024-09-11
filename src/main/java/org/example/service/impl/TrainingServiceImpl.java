package org.example.service.impl;

import org.example.dao.TrainingDao;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.example.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TrainingServiceImpl implements TrainingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final TrainingDao trainingDao;

    @Autowired
    public TrainingServiceImpl(TrainingDao trainingDao) {
        this.trainingDao = trainingDao;
    }

    @Override
    public void createTraining(Training training) {
        LOGGER.info("Creating new training...");
        trainingDao.create(training);
    }

    @Override
    public Training getTraining(Long trainingId) {
        LOGGER.info("Retrieving a training...");
        return trainingDao.select(trainingId);
    }

    @Override
    public List<Training> getAllTrainings() {
        LOGGER.info("Retrieving all trainings...");
        return trainingDao.listAll();
    }

    @Override
    public void updateTraining(Training training) {
        LOGGER.info("Updating the training...");
        trainingDao.updateTraining(training);
    }

    @Override
    public TrainingTypeEntity getTrainingType(String trainingType) {
        return trainingDao.getTrainingType(trainingType);
    }
}

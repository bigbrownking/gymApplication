package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;
import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.dto.responses.training.GetTrainingTypesResponseDto;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainee;
import org.example.models.Trainer;
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
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private TrainingMapper trainingMapper;

    @Autowired
    public TrainingServiceImpl(TrainingDao trainingDao,
                               TrainingMapper trainingMapper,
                               TraineeDao traineeDao,
                               TrainerDao trainerDao) {
        this.trainingDao = trainingDao;
        this.trainingMapper = trainingMapper;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
    }

    @Override
    public void createTraining(CreateTrainingRequestDto createTrainingRequestDto) {
        if(createTrainingRequestDto == null){
            LOGGER.warn("Invalid training...");
        }
        LOGGER.info("Creating new training...");
        Trainee trainee = traineeDao.findByUsername(createTrainingRequestDto.getTraineeUsername()).orElse(null);
        Trainer trainer = trainerDao.findByUsername(createTrainingRequestDto.getTrainerUsername()).orElse(null);

        if(trainee == null){
            LOGGER.warn("No such trainee found...");
            return;
        }
        if(trainer == null){
            LOGGER.warn("No such trainer found...");
            return;
        }

        Training training = trainingMapper.toTraining(createTrainingRequestDto, trainee, trainer);
        trainingDao.create(training);
    }


    @Override
    public List<Training> getAllTrainings() {
        LOGGER.info("Retrieving all trainings...");
        return trainingDao.listAll();
    }

    @Override
    public GetTrainingTypesResponseDto getTrainingTypes() {
        LOGGER.info("Retrieving all training types...");
        List<TrainingTypeEntity> trainingTypeEntities = trainingDao.getTrainingTypes();
        return trainingMapper.toGetTrainingTypesDto(trainingTypeEntities);
    }
}

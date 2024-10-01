package org.example.service.impl;

import org.example.dao.TrainerDao;
import org.example.dto.TraineeDto;
import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerByUsernameRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.dto.responses.trainer.UpdateTrainerResponseDto;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
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
    private TrainerMapper trainerMapper;
    private TraineeMapper traineeMapper;
    private TrainingMapper trainingMapper;

    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao,
                              UserService userService,
                              TrainerMapper trainerMapper,
                              TraineeMapper traineeMapper,
                              TrainingMapper trainingMapper) {
        this.trainerDao = trainerDao;
        this.userService = userService;
        this.trainerMapper = trainerMapper;
        this.traineeMapper = traineeMapper;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto createTrainerRequestDto) {
        LOGGER.debug("Creating new trainer...");
        if (createTrainerRequestDto == null) {
            LOGGER.warn("Invalid trainer...");
            return null;
        }
        List<String> existingUsernames = userService.getAllExistingUsernames();

        LOGGER.debug("Generating new username and password...");
        Trainer trainer = trainerMapper.toTrainer(createTrainerRequestDto);
        String username = Generator.generateUsername(trainer.getFirstName(), trainer.getLastName(), existingUsernames);
        String password = Generator.generatePassword();

        trainer.setUsername(username);
        trainer.setPassword(password);
        trainer.setActive(true);

        trainerDao.create(trainer);
        return trainerMapper.toCreateTrainerDto(trainer);
    }

    @Override
    public UpdateTrainerResponseDto updateTrainer(UpdateTrainerRequestDto updateTrainerRequestDto) {
        LOGGER.debug("Updating trainer...");
        if (updateTrainerRequestDto == null) {
            LOGGER.warn("Invalid trainer...");
            return null;
        }
        if (trainerDao.findByUsername(updateTrainerRequestDto.getUsername()).isEmpty()) {
            LOGGER.warn("Authentication failed...");
            return null;
        }
        Trainer trainer = trainerMapper.toTrainer(updateTrainerRequestDto);
        trainerDao.update(trainer);
        List<TraineeDto> trainees = traineeMapper.convertTraineesToDto(
                trainerDao.allTraineesOfTrainer(
                        trainer.getUsername()));
        return trainerMapper.toUpdateTrainerDto(trainer, trainees);
    }

    @Override
    public GetTrainerByUsernameResponseDto getTrainerByUsername(GetTrainerByUsernameRequestDto getTrainerByUsernameRequestDto) {
        LOGGER.debug("Retrieving a trainer by username...");
        Trainer trainer = trainerDao.findByUsername(getTrainerByUsernameRequestDto.getUsername()).orElse(null);

        List<TraineeDto> trainees = traineeMapper.convertTraineesToDto(
                trainerDao.allTraineesOfTrainer(
                        getTrainerByUsernameRequestDto.getUsername()));

        return trainerMapper.toGetTrainerByUsernameDto(trainer, trainees);
    }

    @Override
    public List<Trainer> getAllTrainers() {
        LOGGER.debug("Retrieving all trainers...");
        return trainerDao.listAll();
    }

    @Override
    public void getTrainerByUsernameAndPassword(LoginRequestDto loginRequestDto) {
        LOGGER.debug("Authenticating trainer...");
        if (!userService.isAuthenticated(loginRequestDto.getUsername(), loginRequestDto.getPassword())) {
            LOGGER.warn("Authentication failed...");
        }
    }


    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        LOGGER.debug("Changing password for trainee...");
        if (!userService.isAuthenticated(changePasswordRequestDto.getUsername(), changePasswordRequestDto.getOldPassword())) {
            LOGGER.warn("Authentication failed...");
            return;
        }
        Trainer trainer = trainerDao.findByUsername(changePasswordRequestDto.getUsername()).orElse(null);
        if(trainer != null){
            trainer.setPassword(changePasswordRequestDto.getPassword());
            trainerDao.update(trainer);
        }

    }

    @Override
    public void activateTrainer(ActivateUserRequestDto activateUserRequestDto) {
        LOGGER.debug("Activating trainer...");
        Trainer trainer = trainerDao.findByUsername(activateUserRequestDto.getUsername()).orElse(null);
        if (trainer == null) {
            LOGGER.warn("There is no such trainer found...");
            return;
        }
        userService.activate(trainer);
    }

    @Override
    public void deactivateTrainer(DeactivateUserRequestDto deactivateUserRequestDto) {
        LOGGER.debug("Deactivating trainee...");
        Trainer trainer = trainerDao.findByUsername(deactivateUserRequestDto.getUsername()).orElse(null);
        if (trainer == null) {
            LOGGER.warn("There is no such trainer found...");
            return;
        }
        userService.deactivate(trainer);
    }

    @Override
    public GetTrainerTrainingListResponseDto getTrainingByCriteria(GetTrainerTrainingListRequestDto getTrainerTrainingListRequestDto) {
        LOGGER.debug("Getting training list by criteria...");
        List<Training> trainings = trainerDao.getTrainingByCriteria(
                getTrainerTrainingListRequestDto.getUsername(),
                getTrainerTrainingListRequestDto.getFromDate(),
                getTrainerTrainingListRequestDto.getToDate(),
                getTrainerTrainingListRequestDto.getTraineeName()
        );

        return trainerMapper.toGetTrainingListDto(
                trainingMapper.convertTrainingsToDto(trainings));
    }

}

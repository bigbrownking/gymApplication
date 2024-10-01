package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.dto.requests.trainee.*;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.requests.user.LoginRequestDto;
import org.example.dto.responses.trainee.*;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
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
    private TraineeMapper traineeMapper;
    private TrainerMapper trainerMapper;
    private TrainingMapper trainingMapper;

    @Autowired
    public TraineeServiceImpl(TraineeDao traineeDao,
                              UserService userService,
                              TraineeMapper traineeMapper,
                              TrainerMapper trainerMapper,
                              TrainingMapper trainingMapper) {
        this.traineeDao = traineeDao;
        this.userService = userService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
    }

    @Override
    public CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto createTraineeRequestDto) {
        LOGGER.debug("Creating new trainee...");
        if (createTraineeRequestDto == null) {
            LOGGER.warn("Invalid trainee...");
            return null;
        }
        List<String> existingUsernames = userService.getAllExistingUsernames();
        LOGGER.debug("Generating new username and password...");

        Trainee trainee = traineeMapper.toTrainee(createTraineeRequestDto);
        String username = Generator.generateUsername(trainee.getFirstName(), trainee.getLastName(), existingUsernames);
        String password = Generator.generatePassword();
        trainee.setUsername(username);
        trainee.setPassword(password);
        trainee.setActive(true);

        traineeDao.create(trainee);
        return traineeMapper.toCreateTraineeDto(trainee);
    }

    @Override
    public UpdateTraineeResponseDto updateTrainee(UpdateTraineeRequestDto updateTraineeRequestDto) {
        LOGGER.debug("Updating trainee...");
        if (updateTraineeRequestDto == null) {
            LOGGER.warn("Invalid trainee...");
            return null;
        }
        if (traineeDao.findByUsername(updateTraineeRequestDto.getUsername()).isEmpty()) {
            LOGGER.warn("Authentication failed...");
            return null;
        }
        Trainee trainee = traineeMapper.toTrainee(updateTraineeRequestDto);
        traineeDao.update(trainee);
        List<TrainerDto> trainers = trainerMapper.convertTrainersToDto(
                traineeDao.getTrainersAssignedToTrainee(
                        trainee.getUsername()));
        return traineeMapper.toUpdateTraineeDto(trainee, trainers);
    }

    @Override
    public void deleteTrainee(DeleteTraineeRequestDto deleteTraineeRequestDto) {
        LOGGER.debug("Deleting trainee...");
        boolean isDeleted = traineeDao.delete(deleteTraineeRequestDto.getUsername());
        if (!isDeleted) {
            LOGGER.warn("Trainee wasn't found...");
        }
    }

    @Override
    public GetTraineeByUsernameResponseDto getTraineeByUsername(GetTraineeByUsernameRequestDto getTraineeByUsernameRequestDto) {
        LOGGER.debug("Retrieving a trainee by username...");
        Trainee trainee = traineeDao.findByUsername(getTraineeByUsernameRequestDto.getUsername()).orElse(null);

        List<TrainerDto> trainers = trainerMapper.convertTrainersToDto(
                traineeDao.getTrainersAssignedToTrainee(
                        getTraineeByUsernameRequestDto.getUsername()));
        return traineeMapper.toGetTraineeByUsernameDto(trainee, trainers);
    }

    @Override
    public List<Trainee> getAllTrainee() {
        LOGGER.debug("Retrieving all trainees...");
        return traineeDao.listAll();
    }

    @Override
    public void getTraineeByUsernameAndPassword(LoginRequestDto loginRequestDto) {
        LOGGER.debug("Authenticating trainee...");
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
        Trainee trainee = traineeDao.findByUsername(changePasswordRequestDto.getUsername()).orElse(null);
        if(trainee != null){
            trainee.setPassword(changePasswordRequestDto.getPassword());
            traineeDao.update(trainee);
        }
    }

    @Override
    public void activateTrainee(ActivateUserRequestDto activateUserRequestDto) {
        LOGGER.debug("Activating trainee...");
        Trainee trainee = traineeDao.findByUsername(activateUserRequestDto.getUsername()).orElse(null);
        if (trainee == null) {
            LOGGER.warn("There is no such trainee found...");
            return;
        }
        userService.activate(trainee);
    }

    @Override
    public void deactivateTrainee(DeactivateUserRequestDto deactivateUserRequestDto) {
        LOGGER.debug("Deactivating trainee...");
        Trainee trainee = traineeDao.findByUsername(deactivateUserRequestDto.getUsername()).orElse(null);
        if (trainee == null) {
            LOGGER.warn("There is no such trainee found...");
            return;
        }
        userService.deactivate(trainee);
    }

    @Override
    public GetTraineeTrainingListResponseDto getTrainingByCriteria(GetTraineeTrainingListRequestDto getTraineeTrainingListRequestDto) {
        LOGGER.debug("Getting training list by criteria...");
        List<Training> trainings = traineeDao.getTrainingByCriteria(
                getTraineeTrainingListRequestDto.getUsername(),
                getTraineeTrainingListRequestDto.getFromDate(),
                getTraineeTrainingListRequestDto.getToDate(),
                getTraineeTrainingListRequestDto.getTrainerName(),
                getTraineeTrainingListRequestDto.getTrainingType()
        );

        return traineeMapper.
                toGetTraineeTrainingListDto(
                        trainingMapper.convertTrainingsToDto(trainings));
    }

    @Override
    public GetNotAssignedTrainersResponseDto getTrainersNotAssignedToTrainee(GetNotAssignedTrainersRequestDto getNotAssignedTrainersRequestDto) {
        LOGGER.debug("Getting trainers not assigned to trainee...");
        List<Trainer> trainers = traineeDao.getTrainersNotAssignedToTrainee(getNotAssignedTrainersRequestDto.getUsername());
        return traineeMapper.toGetNotAssignedTrainersDto(trainerMapper.convertTrainersToDto(trainers));
    }

}

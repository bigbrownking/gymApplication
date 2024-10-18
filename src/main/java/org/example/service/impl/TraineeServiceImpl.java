package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.dto.TrainerDto;
import org.example.dto.requests.trainee.*;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.responses.trainee.*;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.InvalidDataException;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.service.TraineeService;
import org.example.util.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraineeServiceImpl implements TraineeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private final TraineeDao traineeDao;
    private final UserService userService;
    private final TraineeMapper traineeMapper;
    private final TrainerMapper trainerMapper;
    private final TrainingMapper trainingMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TraineeServiceImpl(TraineeDao traineeDao,
                              UserService userService,
                              TraineeMapper traineeMapper,
                              TrainerMapper trainerMapper,
                              TrainingMapper trainingMapper,
                              PasswordEncoder passwordEncoder) {
        this.traineeDao = traineeDao;
        this.userService = userService;
        this.traineeMapper = traineeMapper;
        this.trainerMapper = trainerMapper;
        this.trainingMapper = trainingMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CreateTraineeResponseDto createTrainee(CreateTraineeRequestDto createTraineeRequestDto) {
        try {
            LOGGER.debug("Creating new trainee...");
            if (createTraineeRequestDto == null) {
                LOGGER.warn("Invalid request...");
                return null;
            }
            List<String> existingUsernames = userService.getAllExistingUsernames();
            LOGGER.debug("Generating new username and password...");

            Trainee trainee = traineeMapper.toTrainee(createTraineeRequestDto);
            String username = Generator.generateUsername(trainee.getFirstName(), trainee.getLastName(), existingUsernames);
            String rawPassword = Generator.generatePassword();

            trainee.setUsername(username);
            trainee.setPassword(passwordEncoder.encode(rawPassword));
            trainee.setActive(true);
            traineeDao.create(trainee);

            CreateTraineeResponseDto responseDto = traineeMapper.toCreateTraineeDto(trainee);
            responseDto.setPassword(rawPassword);

            return responseDto;
        } catch (InvalidDataException e) {
            LOGGER.warn("Data is invalid...");
            throw e;
        }
    }

    @Override
    public UpdateTraineeResponseDto updateTrainee(UpdateTraineeRequestDto updateTraineeRequestDto) {
        try {
            LOGGER.debug("Updating trainee...");
            if (updateTraineeRequestDto == null) {
                LOGGER.warn("Invalid request...");
                throw new InvalidDataException("Data is invalid...");
            }
            if (traineeDao.findByUsername(updateTraineeRequestDto.getUsername()).isEmpty()) {
                LOGGER.warn("Authentication failed...");
                throw new EntityNotFoundException("Entity not found...");
            }
            Trainee trainee = traineeMapper.toTrainee(updateTraineeRequestDto);
            if (!userService.isValid(trainee)) {
                LOGGER.warn("For some reasons trainee is not valid...");
                throw new InvalidDataException("Trainee is invalid...");
            }
            traineeDao.update(trainee);
            List<TrainerDto> trainers = trainerMapper.convertTrainersToDto(
                    traineeDao.getTrainersAssignedToTrainee(
                            trainee.getUsername()));
            return traineeMapper.toUpdateTraineeDto(trainee, trainers);
        } catch (EntityNotFoundException | InvalidDataException e) {
            LOGGER.warn("Entity not found or data is invalid...");
            return null;
        }
    }

    @Override
    public void deleteTrainee(DeleteTraineeRequestDto deleteTraineeRequestDto) {
        try {
            LOGGER.debug("Deleting trainee...");
            if (deleteTraineeRequestDto == null) {
                LOGGER.warn("Invalid request...");
                return;
            }
            boolean isDeleted = traineeDao.delete(deleteTraineeRequestDto.getUsername());
            if (!isDeleted) {
                LOGGER.warn("There is no such trainee found...");
            }
        } catch (EntityNotFoundException | InvalidDataException e) {
            LOGGER.warn("Entity not found or data is invalid...");
        }
    }

    @Override
    public GetTraineeByUsernameResponseDto getTraineeByUsername(GetTraineeByUsernameRequestDto getTraineeByUsernameRequestDto) {
        LOGGER.debug("Retrieving a trainee by username...");
        if (getTraineeByUsernameRequestDto == null) {
            LOGGER.warn("Invalid request...");
            throw new InvalidDataException("Data is invalid");
        }
        Trainee trainee = traineeDao.findByUsername(getTraineeByUsernameRequestDto.getUsername()).orElse(null);
        if (trainee == null) {
            LOGGER.warn("There is no such trainee found...");
            throw new EntityNotFoundException("Trainee not found");
        }
        List<TrainerDto> trainers = trainerMapper.convertTrainersToDto(
                traineeDao.getTrainersAssignedToTrainee(
                        getTraineeByUsernameRequestDto.getUsername()));
        return traineeMapper.toGetTraineeByUsernameDto(trainee, trainers);
    }

    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        try {
            LOGGER.debug("Changing password for trainee...");
            if (changePasswordRequestDto == null) {
                LOGGER.warn("Invalid request...");
                return;
            }
            Trainee trainee = traineeDao.findByUsername(changePasswordRequestDto.getUsername()).orElse(null);
            if (trainee == null) {
                LOGGER.warn("There is no such trainee found...");
                return;
            }

            trainee.setPassword(passwordEncoder.encode(changePasswordRequestDto.getPassword()));
            traineeDao.update(trainee);
        } catch (EntityNotFoundException | InvalidDataException e) {
            LOGGER.warn("Entity not found or data is invalid...");
        }
    }

    @Override
    public void activateTrainee(ActivateUserRequestDto activateUserRequestDto) {
        LOGGER.debug("Activating trainee...");
        if (activateUserRequestDto == null) {
            LOGGER.warn("Invalid request...");
            return;
        }
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
        if (deactivateUserRequestDto == null) {
            LOGGER.warn("Invalid request...");
            return;
        }
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
        if (getTraineeTrainingListRequestDto == null) {
            LOGGER.warn("Invalid request...");
            return null;
        }
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
        try {
            LOGGER.debug("Getting trainers not assigned to trainee...");
            if (getNotAssignedTrainersRequestDto == null) {
                LOGGER.warn("Invalid request...");
                return null;
            }
            try {
                List<Trainer> trainers = traineeDao.getTrainersNotAssignedToTrainee(getNotAssignedTrainersRequestDto.getUsername());
                return traineeMapper.toGetNotAssignedTrainersDto(trainerMapper.convertTrainersToDto(trainers));
            } catch (Exception e) {
                LOGGER.warn("There is no such trainee found...");
                return null;
            }
        } catch (EntityNotFoundException | InvalidDataException e) {
            LOGGER.warn("Entity not found or data is invalid...");
            return null;
        }
    }

    @Override
    public String getPasswordFromTrainee(String username) {
        try {
            if (username != null) {
                return traineeDao.findByUsername(username).orElse(null).getPassword();
            }
            return null;
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Entity not found...");
            return null;
        }
    }


}

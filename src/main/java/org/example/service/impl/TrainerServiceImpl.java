package org.example.service.impl;

import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;
import org.example.dto.TraineeDto;
import org.example.dto.requests.trainer.CreateTrainerRequestDto;
import org.example.dto.requests.trainer.GetTrainerByUsernameRequestDto;
import org.example.dto.requests.trainer.GetTrainerTrainingListRequestDto;
import org.example.dto.requests.trainer.UpdateTrainerRequestDto;
import org.example.dto.requests.user.ActivateUserRequestDto;
import org.example.dto.requests.user.ChangePasswordRequestDto;
import org.example.dto.requests.user.DeactivateUserRequestDto;
import org.example.dto.responses.trainer.CreateTrainerResponseDto;
import org.example.dto.responses.trainer.GetTrainerByUsernameResponseDto;
import org.example.dto.responses.trainer.GetTrainerTrainingListResponseDto;
import org.example.dto.responses.trainer.UpdateTrainerResponseDto;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.InvalidDataException;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.example.service.TrainerService;
import org.example.util.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerServiceImpl implements TrainerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private final TrainerDao trainerDao;
    private final UserService userService;
    private final TrainerMapper trainerMapper;
    private final TraineeMapper traineeMapper;
    private final TrainingMapper trainingMapper;
    private final TrainingDao trainingDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao,
                              UserService userService,
                              TrainerMapper trainerMapper,
                              TraineeMapper traineeMapper,
                              TrainingMapper trainingMapper,
                              TrainingDao trainingDao,
                              PasswordEncoder passwordEncoder) {
        this.trainerDao = trainerDao;
        this.userService = userService;
        this.trainerMapper = trainerMapper;
        this.traineeMapper = traineeMapper;
        this.trainingMapper = trainingMapper;
        this.trainingDao = trainingDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto createTrainerRequestDto) {
        try {
            LOGGER.debug("Creating new trainer...");
            if (createTrainerRequestDto == null) {
                LOGGER.warn("Invalid request...");
                return null;
            }
            String requestedSpecialization = createTrainerRequestDto.getSpecialization().getTrainingTypeName();
            List<String> trainingTypes = trainingDao.getTrainingTypes().stream()
                    .map(TrainingTypeEntity::getTrainingTypeName)
                    .toList();
            LOGGER.warn("TRAINING TYPES: {}", trainingTypes);
            if (!trainingTypes.contains(requestedSpecialization)) {
                LOGGER.warn("Invalid specialization: There is no such training type...");
                return null; // Consider throwing an exception instead
            }
            List<String> existingUsernames = userService.getAllExistingUsernames();
            LOGGER.debug("Generating new username and password...");

            Trainer trainer = trainerMapper.toTrainer(createTrainerRequestDto);
            String username = Generator.generateUsername(trainer.getFirstName(), trainer.getLastName(), existingUsernames);
            String rawPassword = Generator.generatePassword();

            trainer.setUsername(username);
            trainer.setPassword(passwordEncoder.encode(rawPassword));
            trainer.setActive(true);
            trainer.setSpecialization(createTrainerRequestDto.getSpecialization());
            trainerDao.create(trainer);

            CreateTrainerResponseDto responseDto = trainerMapper.toCreateTrainerDto(trainer);
            responseDto.setPassword(rawPassword);

            return responseDto;
        } catch (InvalidDataException e) {
            LOGGER.warn("An error occurred, while retrieving the training types: " + e.getMessage());
            return null;
        }
    }
    @Override
    public UpdateTrainerResponseDto updateTrainer(UpdateTrainerRequestDto updateTrainerRequestDto) {
        try {
            LOGGER.debug("Updating trainer...");
            if (updateTrainerRequestDto == null) {
                LOGGER.warn("Invalid request...");
                return null;
            }
            if (trainerDao.findByUsername(updateTrainerRequestDto.getUsername()).isEmpty()) {
                LOGGER.warn("Authentication failed...");
                return null;
            }
            Trainer trainer = trainerMapper.toTrainer(updateTrainerRequestDto);
            if (!userService.isValid(trainer)) {
                LOGGER.warn("For some reasons trainer is not valid...");
                return null;
            }
            trainerDao.update(trainer);
            List<TraineeDto> trainees = traineeMapper.convertTraineesToDto(
                    trainerDao.allTraineesOfTrainer(
                            trainer.getUsername()));
            return trainerMapper.toUpdateTrainerDto(trainer, trainees);
        } catch (EntityNotFoundException | InvalidDataException e) {
            LOGGER.warn("Entity not found or data is invalid...");
            return null;
        }
    }

    @Override
    public GetTrainerByUsernameResponseDto getTrainerByUsername(GetTrainerByUsernameRequestDto getTrainerByUsernameRequestDto) {
        try {
            LOGGER.debug("Retrieving a trainer by username...");
            if (getTrainerByUsernameRequestDto == null) {
                LOGGER.warn("Invalid request...");
                return null;
            }
            Trainer trainer = trainerDao.findByUsername(getTrainerByUsernameRequestDto.getUsername()).orElse(null);
            if (trainer == null) {
                LOGGER.warn("There is no such trainer found...");
                return null;
            }
            List<TraineeDto> trainees = traineeMapper.convertTraineesToDto(
                    trainerDao.allTraineesOfTrainer(
                            getTrainerByUsernameRequestDto.getUsername()));
            return trainerMapper.toGetTrainerByUsernameDto(trainer, trainees);
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Entity not found...");
            return null;
        }
    }

    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        try {
            LOGGER.debug("Changing password for trainee...");
            if (changePasswordRequestDto == null) {
                LOGGER.warn("Invalid request...");
                return;
            }
            Trainer trainer = trainerDao.findByUsername(changePasswordRequestDto.getUsername()).orElse(null);
            if (trainer == null) {
                LOGGER.warn("There is no such trainer found...");
                return;
            }

            trainer.setPassword(passwordEncoder.encode(changePasswordRequestDto.getPassword()));
            trainerDao.update(trainer);
        } catch (EntityNotFoundException | InvalidDataException e) {
            LOGGER.warn("Entity not found or data is invalid...");
        }
    }

    @Override
    public void activateTrainer(ActivateUserRequestDto activateUserRequestDto){
        LOGGER.debug("Activating trainer...");
        if(activateUserRequestDto == null){
            LOGGER.warn("Invalid request...");
            return;
        }
        Trainer trainer = trainerDao.findByUsername(activateUserRequestDto.getUsername()).orElse(null);
        if (trainer == null) {
            LOGGER.warn("There is no such trainer found...");
            return;
        }
        userService.activate(trainer);
    }

    @Override
    public void deactivateTrainer(DeactivateUserRequestDto deactivateUserRequestDto){
        LOGGER.debug("Deactivating trainee...");
        if(deactivateUserRequestDto == null){
            LOGGER.warn("Invalid request...");
            return;
        }
        Trainer trainer = trainerDao.findByUsername(deactivateUserRequestDto.getUsername()).orElse(null);
        if (trainer == null) {
            LOGGER.warn("There is no such trainer found...");
            return;
        }
        userService.deactivate(trainer);
    }

    @Override
    public GetTrainerTrainingListResponseDto getTrainingByCriteria(GetTrainerTrainingListRequestDto getTrainerTrainingListRequestDto){
        LOGGER.debug("Getting training list by criteria...");
        if(getTrainerTrainingListRequestDto == null){
            LOGGER.warn("Invalid request...");
            return null;
        }
        List<Training> trainings = trainerDao.getTrainingByCriteria(
                getTrainerTrainingListRequestDto.getUsername(),
                getTrainerTrainingListRequestDto.getFromDate(),
                getTrainerTrainingListRequestDto.getToDate(),
                getTrainerTrainingListRequestDto.getTraineeName()
        );

        return trainerMapper.toGetTrainingListDto(
                trainingMapper.convertTrainingsToDto(trainings));
    }

    @Override
    public String getPasswordFromTrainer(String username) {
        try {
            return trainerDao.findByUsername(username)
                    .map(Trainer::getPassword)
                    .orElse(null);
        } catch (EntityNotFoundException e) {
            LOGGER.warn("Entity not found...");
            return null;
        }
    }

}

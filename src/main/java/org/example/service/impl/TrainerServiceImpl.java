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
    private TrainingDao trainingDao;

    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao,
                              UserService userService,
                              TrainerMapper trainerMapper,
                              TraineeMapper traineeMapper,
                              TrainingMapper trainingMapper,
                              TrainingDao trainingDao) {
        this.trainerDao = trainerDao;
        this.userService = userService;
        this.trainerMapper = trainerMapper;
        this.traineeMapper = traineeMapper;
        this.trainingMapper = trainingMapper;
        this.trainingDao = trainingDao;
    }

    @Override
    public CreateTrainerResponseDto createTrainer(CreateTrainerRequestDto createTrainerRequestDto) throws Exception {
        LOGGER.debug("Creating new trainer...");
        if (createTrainerRequestDto == null) {
            LOGGER.warn("Invalid request...");
            return null;
        }
        if(!trainingDao.getTrainingTypes().contains(createTrainerRequestDto.getSpecialization())){
            LOGGER.warn("There is no such training type...");
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
        trainer.setSpecialization(createTrainerRequestDto.getSpecialization());
        trainerDao.create(trainer);
        return trainerMapper.toCreateTrainerDto(trainer);
    }

    @Override
    public UpdateTrainerResponseDto updateTrainer(UpdateTrainerRequestDto updateTrainerRequestDto) throws Exception {
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
        if(!userService.isValid(trainer)) {
            LOGGER.warn("For some reasons trainer is not valid...");
            return null;
        }
        trainerDao.update(trainer);
        List<TraineeDto> trainees = traineeMapper.convertTraineesToDto(
                trainerDao.allTraineesOfTrainer(
                        trainer.getUsername()));
        return trainerMapper.toUpdateTrainerDto(trainer, trainees);
    }

    @Override
    public GetTrainerByUsernameResponseDto getTrainerByUsername(GetTrainerByUsernameRequestDto getTrainerByUsernameRequestDto) throws Exception {
        LOGGER.debug("Retrieving a trainer by username...");
        if(getTrainerByUsernameRequestDto == null){
            LOGGER.warn("Invalid request...");
            return null;
        }
        Trainer trainer = trainerDao.findByUsername(getTrainerByUsernameRequestDto.getUsername()).orElse(null);
        if(trainer == null){
            LOGGER.warn("There is no such trainer found...");
            return null;
        }
        List<TraineeDto> trainees = traineeMapper.convertTraineesToDto(
                trainerDao.allTraineesOfTrainer(
                        getTrainerByUsernameRequestDto.getUsername()));
        return trainerMapper.toGetTrainerByUsernameDto(trainer, trainees);
    }
    @Override
    public void getTrainerByUsernameAndPassword(LoginRequestDto loginRequestDto) throws Exception {
        LOGGER.debug("Authenticating trainer...");
        if(loginRequestDto == null){
            LOGGER.warn("Invalid request...");
            return;
        }
        if (!userService.isAuthenticated(loginRequestDto.getUsername(), loginRequestDto.getPassword())) {
            LOGGER.warn("Authentication failed...");
        }
    }


    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) throws Exception {
        LOGGER.debug("Changing password for trainee...");
        if(changePasswordRequestDto == null){
            LOGGER.warn("Invalid request...");
            return;
        }
        if (!userService.isAuthenticated(changePasswordRequestDto.getUsername(), changePasswordRequestDto.getOldPassword())) {
            LOGGER.warn("Authentication failed...");
            return;
        }
        Trainer trainer = trainerDao.findByUsername(changePasswordRequestDto.getUsername()).orElse(null);
        if(trainer == null){
            LOGGER.warn("There is no such trainer found...");
            return;
        }
        trainer.setPassword(changePasswordRequestDto.getPassword());
        trainerDao.update(trainer);
    }

    @Override
    public void activateTrainer(ActivateUserRequestDto activateUserRequestDto) throws Exception {
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
    public void deactivateTrainer(DeactivateUserRequestDto deactivateUserRequestDto) throws Exception {
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
    public GetTrainerTrainingListResponseDto getTrainingByCriteria(GetTrainerTrainingListRequestDto getTrainerTrainingListRequestDto) throws Exception {
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

}

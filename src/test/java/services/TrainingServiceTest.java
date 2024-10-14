package org.example.service.impl;

import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.TrainingDao;
import org.example.dto.requests.training.CreateTrainingRequestDto;
import org.example.dto.responses.training.GetTrainingTypesResponseDto;
import org.example.exceptions.InvalidDataException;
import org.example.mapper.TrainingMapper;
import org.example.models.Trainee;
import org.example.models.Trainer;
import org.example.models.Training;
import org.example.models.TrainingTypeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingMapper trainingMapper;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTraining_ShouldCreateTrainingSuccessfully() {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto();
        requestDto.setTraineeUsername("traineeUser");
        requestDto.setTrainerUsername("trainerUser");

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        Training training = new Training();

        when(traineeDao.findByUsername("traineeUser")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainerUser")).thenReturn(Optional.of(trainer));
        when(trainingMapper.toTraining(any(CreateTrainingRequestDto.class), any(Trainee.class), any(Trainer.class))).thenReturn(training);

        trainingService.createTraining(requestDto);

        verify(trainingDao).create(training);
    }

    @Test
    void createTraining_ShouldNotCreateTraining_WhenRequestIsNull() {
        trainingService.createTraining(null);

        verify(traineeDao, never()).findByUsername(any());
        verify(trainerDao, never()).findByUsername(any());
        verify(trainingDao, never()).create(any());
    }

    @Test
    void createTraining_ShouldNotCreateTraining_WhenTraineeNotFound() {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto();
        requestDto.setTraineeUsername("nonexistentTrainee");
        requestDto.setTrainerUsername("trainerUser");

        Trainer trainer = new Trainer();
        when(trainerDao.findByUsername("trainerUser")).thenReturn(Optional.of(trainer));
        when(traineeDao.findByUsername("nonexistentTrainee")).thenReturn(Optional.empty());

        trainingService.createTraining(requestDto);

        verify(trainingDao, never()).create(any());
    }

    @Test
    void createTraining_ShouldNotCreateTraining_WhenTrainerNotFound() {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto();
        requestDto.setTraineeUsername("traineeUser");
        requestDto.setTrainerUsername("nonexistentTrainer");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("traineeUser")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("nonexistentTrainer")).thenReturn(Optional.empty());

        trainingService.createTraining(requestDto);

        verify(trainingDao, never()).create(any());
    }

    @Test
    void getTrainingTypes_ShouldReturnTrainingTypesSuccessfully() {
        List<TrainingTypeEntity> trainingTypes = Collections.singletonList(new TrainingTypeEntity("Yoga"));
        GetTrainingTypesResponseDto expectedDto = new GetTrainingTypesResponseDto();

        when(trainingDao.getTrainingTypes()).thenReturn(trainingTypes);
        when(trainingMapper.toGetTrainingTypesDto(trainingTypes)).thenReturn(expectedDto);

        GetTrainingTypesResponseDto actualDto = trainingService.getTrainingTypes();

        assertEquals(expectedDto, actualDto);
    }

    @Test
    void getTrainingTypes_ShouldReturnNull_WhenDataIsInvalid() {
        when(trainingDao.getTrainingTypes()).thenThrow(new InvalidDataException("Invalid data"));

        GetTrainingTypesResponseDto actualDto = trainingService.getTrainingTypes();

        assertNull(actualDto);
    }
}

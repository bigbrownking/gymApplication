package services;

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
import org.example.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    void createTraining_ValidRequest() throws Exception {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto();
        requestDto.setTraineeUsername("traineeUser");
        requestDto.setTrainerUsername("trainerUser");

        Trainee trainee = new Trainee();
        Trainer trainer = new Trainer();
        Training training = new Training();

        when(traineeDao.findByUsername("traineeUser")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainerUser")).thenReturn(Optional.of(trainer));
        when(trainingMapper.toTraining(requestDto, trainee, trainer)).thenReturn(training);

        trainingService.createTraining(requestDto);

        verify(trainingDao).create(training);
    }

    @Test
    void createTraining_InvalidTrainee() throws Exception {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto();
        requestDto.setTraineeUsername("traineeUser");
        requestDto.setTrainerUsername("trainerUser");

        when(traineeDao.findByUsername("traineeUser")).thenReturn(Optional.empty());

        trainingService.createTraining(requestDto);

        verify(trainingDao, never()).create(any());
    }

    @Test
    void createTraining_InvalidTrainer() throws Exception {
        CreateTrainingRequestDto requestDto = new CreateTrainingRequestDto();
        requestDto.setTraineeUsername("traineeUser");
        requestDto.setTrainerUsername("trainerUser");

        Trainee trainee = new Trainee();
        when(traineeDao.findByUsername("traineeUser")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("trainerUser")).thenReturn(Optional.empty());

        trainingService.createTraining(requestDto);

        verify(trainingDao, never()).create(any());
    }


    @Test
    void getTrainingTypes() throws Exception {
        List<TrainingTypeEntity> trainingTypes = Collections.singletonList(new TrainingTypeEntity());
        GetTrainingTypesResponseDto expectedResponse = new GetTrainingTypesResponseDto();

        when(trainingDao.getTrainingTypes()).thenReturn(trainingTypes);
        when(trainingMapper.toGetTrainingTypesDto(trainingTypes)).thenReturn(expectedResponse);

        GetTrainingTypesResponseDto result = trainingService.getTrainingTypes();

        assertThat(result).isEqualTo(expectedResponse);
        verify(trainingDao).getTrainingTypes();
    }
}
